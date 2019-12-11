package com.home.genesis.logic.services;

import com.home.genesis.Constants;
import com.home.genesis.logic.CellType;
import com.home.genesis.logic.actions.*;
import com.home.genesis.logic.context.SimulatorContext;
import com.home.genesis.logic.entity.*;
import com.home.genesis.logic.entity.results.ActionResultBundle;
import com.home.genesis.representation.Styles;

import java.util.*;

import static com.home.genesis.Constants.DEFAULT_ACTION_NUMBER;

public class BotService {

    private FoodService foodService;
    private PoisonService poisonService;
    private CellService cellService;

    public BotService() {
        this.foodService = new FoodService();
        this.cellService = new CellService();
        this.poisonService = new PoisonService();
    }

    public boolean handleBotDecisionTaking(final SingleBot bot, final ActionResultBundle actionResultBundle) {
        final int currentAction = bot.getDnaCommands().get(bot.getCurrentStep());
        if (currentAction >= (DEFAULT_ACTION_NUMBER * 4)) {
            return doConditionalPointerMove(bot, currentAction);
        } else if (currentAction >= (DEFAULT_ACTION_NUMBER * 3)) {
            return doTurnAction(bot, currentAction, actionResultBundle);
        } else if (currentAction >= (DEFAULT_ACTION_NUMBER * 2)) {
            return doCheckAction(bot, currentAction);
        } else if (currentAction >= DEFAULT_ACTION_NUMBER) {
            return doGrabAction(bot, currentAction, actionResultBundle);
        } else {
            return doMoveAction(bot, currentAction, actionResultBundle);
        }
    }

    public void handleBotDeathOrAction(final SingleBot singleBot,
                                       final ListIterator<SingleBot> singleBotListIterator,
                                       final ActionResultBundle actionResultBundle) {
        if (singleBot.getHealth() > 1) {
            decreaseBotHealth(singleBot, actionResultBundle);
            singleBot.setActionsCounter(singleBot.getActionsCounter() + 1);
        } else {
            final int positionX = singleBot.getPositionX();
            final int positionY = singleBot.getPositionY();
            Cell cell = cellService.createCell(positionX, positionY, CellType.EMPTY);
            SimulatorContext.getInstance().getCellsArray()[positionX][positionY] = cell;
            singleBotListIterator.remove();
            actionResultBundle.addBotActionResult(ActionsResult.CHANGE_TILE, positionX, positionY, Styles.EMPTY.getStyleName());
            actionResultBundle.addBotActionResult(ActionsResult.CHANGE_TEXT, positionX, positionY, "");
        }
    }

    public void handleNewBotsCreation(final ListIterator<SingleBot> singleBotListIterator,
                                      final ActionResultBundle actionResultBundle) {
        List<SingleBot> bots = new ArrayList<>(SimulatorContext.getInstance().getBots());
        int genomeModificationCounter = 0;
        for (SingleBot bot : bots) {
            copyBots(bot, singleBotListIterator, actionResultBundle, genomeModificationCounter);
        }
    }

    public void updateStatisticsResults(final List<SingleBot> bots,
                                        final int generationNumber,
                                        final ActionResultBundle actionResultBundle) {
        Optional<SingleBot> first = bots.stream().max(Comparator.comparingInt(SingleBot::getActionsCounter));
        first.ifPresent(bot -> {
            actionResultBundle.addGenomeResult(bot.getDnaCommands());
            actionResultBundle.setGenerationCounter(generationNumber);
            actionResultBundle.setBotBestActionCounter(bot.getActionsCounter());
        });
    }

    private void copyBots(final SingleBot bot,
                          final ListIterator<SingleBot> singleBotListIterator,
                          final ActionResultBundle actionResultBundle,
                          int genomeModificationCounter) {
        // we subtract 1 as we currently have bots, 64max / 8min
        // equal 8, but each bot should produce 7 copies
        final int copiesNumber = (Constants.BOT_MAX_NUMBER / Constants.BOT_MIN_NUMBER) - 1;
        for (int i = 0; i < copiesNumber; i++) {
            final Cell cell = cellService.getRandomEmptyCell();
            final int positionX = cell.getPositionX();
            final int positionY = cell.getPositionY();
            SingleBot copiedBot = new SingleBot(bot.getDnaCommands(), positionX, positionY);
            if (genomeModificationCounter < Constants.MAX_MUTATIONS_PER_GENERATION && tryToModifyGenome(copiedBot)) {
                genomeModificationCounter++;
            }
            SimulatorContext.getInstance().getCellsArray()[positionX][positionY] = copiedBot;
            singleBotListIterator.add(copiedBot);
            actionResultBundle.addBotActionResult(ActionsResult.CHANGE_TILE, positionX, positionY, Styles.BOT.getStyleName());
            actionResultBundle.addBotActionResult(ActionsResult.CHANGE_TEXT, positionX, positionY, concatHealthAndDirection(copiedBot));
        }
    }

    private String concatHealthAndDirection(final SingleBot bot) {
        final int health = bot.getHealth();
        final int direction = bot.getDirection();
        TurnActionType turnActionType = TurnActionType.values()[direction];
        return String.format("%s%s", turnActionType.getArrowSign(), health);
    }

    private boolean tryToModifyGenome(final SingleBot bot) {
        final Random random = new Random(System.currentTimeMillis());
        final boolean doChange = random.nextBoolean();
        if (doChange) {
            final int commandNumber = random.nextInt(Constants.BOT_DNA_COMMANDS);
            final int commandValue = random.nextInt(Constants.BOT_DNA_COMMANDS);
            bot.getDnaCommands().set(commandNumber, commandValue);
        }
        return doChange;
    }

    private void decreaseBotHealth(final SingleBot bot, final ActionResultBundle actionResultBundle) {
        bot.setHealth(bot.getHealth() - Constants.BOT_CONSUMES_CALORIES);
        actionResultBundle.addBotActionResult(ActionsResult.CHANGE_TEXT, bot.getPositionX(), bot.getPositionY(), concatHealthAndDirection(bot));
    }

    //from 32 to 63 we just move the pointer to this number
    private boolean doConditionalPointerMove(final SingleBot bot, final int currentAction) {
        updateBotActionPointer(bot, currentAction);
        return false;
    }

    private boolean doTurnAction(final SingleBot bot, final int currentAction, final ActionResultBundle actionResultBundle) {
        // we decrease in order to access enum by ordinal
        final int valueModifiedByCurrentDirection = updateWithCurrentDirection(currentAction - DEFAULT_ACTION_NUMBER * 3, bot.getDirection());
        TurnActionType turnActionType = TurnActionType.values()[valueModifiedByCurrentDirection];
        int actionPointerMoveTo = handleTurnActionResult(turnActionType, bot, actionResultBundle);
        updateBotActionPointer(bot, actionPointerMoveTo);
        return turnActionType.getCommandType() == CommandType.TERMINAL;
    }

    private boolean doCheckAction(final SingleBot bot, final int currentAction) {
        final int valueModifiedByCurrentDirection = updateWithCurrentDirection(currentAction - DEFAULT_ACTION_NUMBER * 2, bot.getDirection());
        CheckActionType checkActionType = CheckActionType.values()[valueModifiedByCurrentDirection];
        int actionPointerMoveTo = handleCheckActionResult(checkActionType, bot);
        updateBotActionPointer(bot, actionPointerMoveTo);
        return checkActionType.getCommandType() == CommandType.TERMINAL;
    }

    private boolean doGrabAction(final SingleBot bot, final int currentAction, final ActionResultBundle actionResultBundle) {
        final int valueModifiedByCurrentDirection = updateWithCurrentDirection(currentAction - DEFAULT_ACTION_NUMBER, bot.getDirection());
        GrabActionType grabActionType = GrabActionType.values()[valueModifiedByCurrentDirection];
        int actionPointerMoveTo = handleGrabActionResult(grabActionType, bot, actionResultBundle);
        updateBotActionPointer(bot, actionPointerMoveTo);
        return grabActionType.getCommandType() == CommandType.TERMINAL;
    }

    private boolean doMoveAction(final SingleBot bot, final int currentAction, final ActionResultBundle actionResultBundle) {
        final int valueModifiedByCurrentDirection = updateWithCurrentDirection(currentAction, bot.getDirection());
        MoveActionType moveActionType = MoveActionType.values()[valueModifiedByCurrentDirection];
        int actionPointerMoveTo = handleMoveActionResult(moveActionType, bot, actionResultBundle);
        updateBotActionPointer(bot, actionPointerMoveTo);
        return moveActionType.getCommandType() == CommandType.TERMINAL;
    }

    private int handleTurnActionResult(final TurnActionType turnActionType, final SingleBot bot, final ActionResultBundle actionResultBundle) {
        bot.setDirection(turnActionType.getTurnDirection());
        actionResultBundle.addBotActionResult(ActionsResult.CHANGE_TEXT, bot.getPositionX(), bot.getPositionY(), concatHealthAndDirection(bot));
        return 1;
    }

    private int updateWithCurrentDirection(final int actionNumber, final int turnNumber) {
        /* if for move operation we have the following actionNumbers
        7 0 1
        6 B 2
        5 4 3
        then if direction 3, and move is 7, then 7 should be on position 1
        to achieve that we can calc 3 + 7 = 10 - 8 = 2
        if the number is lesser then 7 -> we return the sum
        */
        final int intermediateResult = actionNumber + turnNumber;
        if (intermediateResult > 7) {
            return intermediateResult - 7;
        } else {
            return intermediateResult;
        }
    }

    private int handleCheckActionResult(final CheckActionType checkActionType, final SingleBot bot) {
        final int resultX = bot.getPositionX() + checkActionType.getX();
        final int resultY = bot.getPositionY() + checkActionType.getY();
        final Cell destinationCell =
                SimulatorContext.getInstance().getCellsArray()[resultX][resultY];
        final CellType cellType = destinationCell.getCellType();
        if (cellType == CellType.POISON) {
            return 1;
        } else if (cellType == CellType.OBSTACLE) {
            return 2;
        } else if (cellType == CellType.BOT) {
            return 3;
        } else if (cellType == CellType.FOOD) {
            return 4;
        } else {
            return 5;
        }
    }

    private int handleGrabActionResult(final GrabActionType grabActionType,
                                       final SingleBot bot,
                                       final ActionResultBundle actionResultBundle) {
        final int resultX = bot.getPositionX() + grabActionType.getX();
        final int resultY = bot.getPositionY() + grabActionType.getY();
        final Cell destinationCell = SimulatorContext.getInstance().getCellsArray()[resultX][resultY];
        final CellType cellType = destinationCell.getCellType();
        if (cellType == CellType.POISON) {
            handleNeutralizePoison(resultX, resultY, actionResultBundle);
            return 1;
        } else if (cellType == CellType.OBSTACLE) {
            return 2;
        } else if (cellType == CellType.BOT) {
            return 3;
        } else if (cellType == CellType.FOOD) {
            handleBotGrabAndEats(resultX, resultY, bot, actionResultBundle);
            return 4;
        } else {
            return 5;
        }
    }

    private void handleNeutralizePoison(int resultX, int resultY, ActionResultBundle actionResultBundle) {
        Poison poisonCell = (Poison) SimulatorContext.getInstance().getCellsArray()[resultX][resultY];
        poisonService.removePoison(poisonCell);
        Food food = (Food) cellService.createCell(resultX, resultY, CellType.FOOD);
        foodService.addFood(food);
        actionResultBundle.addBotActionResult(ActionsResult.CHANGE_TILE, resultX, resultY, Styles.FOOD.getStyleName());
        if (SimulatorContext.getInstance().getPoisonNumber() < Constants.POISON_NUMBER) {
            Cell newPoisonCell = poisonService.generatePoison();
            actionResultBundle.addBotActionResult(ActionsResult.CHANGE_TILE, newPoisonCell.getPositionX(), newPoisonCell.getPositionY(), Styles.POISON.getStyleName());
        }
    }

    private int handleMoveActionResult(final MoveActionType moveActionType,
                                       final SingleBot bot,
                                       final ActionResultBundle actionResultBundle) {
        final int resultX = bot.getPositionX() + moveActionType.getX();
        final int resultY = bot.getPositionY() + moveActionType.getY();
        final Cell destinationCell = SimulatorContext.getInstance().getCellsArray()[resultX][resultY];
        final CellType cellType = destinationCell.getCellType();
        if (cellType == CellType.POISON) {
            handleBotDies(resultX, resultY, bot, actionResultBundle);
            return 1;
        } else if (cellType == CellType.OBSTACLE) {
            return 2;
        } else if (cellType == CellType.BOT) {
            return 3;
        } else if (cellType == CellType.FOOD) {
            handleBotEatsAndMove(resultX, resultY, bot, actionResultBundle);
            return 4;
        } else {
            handleMoveBot(resultX, resultY, bot, actionResultBundle);
            return 5;
        }
    }

    private void updateBotActionPointer(SingleBot bot, int actionPointerMoveTo) {
        final int currentPointerPosition = bot.getCurrentStep();
        final int newPointerPosition = currentPointerPosition + actionPointerMoveTo;
        if (newPointerPosition >= Constants.BOT_DNA_COMMANDS) {
            bot.setCurrentStep(newPointerPosition - Constants.BOT_DNA_COMMANDS);
        } else {
            bot.setCurrentStep(newPointerPosition);
        }
    }

    private void handleBotDies(int resultX, int resultY, SingleBot bot, ActionResultBundle actionResultBundle) {
        final Poison poisonCell = (Poison) SimulatorContext.getInstance().getCellsArray()[resultX][resultY];
        bot.setHealth(-1);
        poisonService.removePoison(poisonCell);
        actionResultBundle.addBotActionResult(ActionsResult.REMOVE, bot.getPositionX(), bot.getPositionY(), null);
        actionResultBundle.addBotActionResult(ActionsResult.CHANGE_TEXT, bot.getPositionX(), bot.getPositionY(), "");
        actionResultBundle.addBotActionResult(ActionsResult.REMOVE, resultX, resultY, null);
        if (SimulatorContext.getInstance().getPoisonNumber() < Constants.POISON_NUMBER) {
            Poison newPoisonCell = (Poison) poisonService.generatePoison();
            actionResultBundle.addBotActionResult(ActionsResult.CHANGE_TILE, newPoisonCell.getPositionX(),
                    newPoisonCell.getPositionY(), Styles.POISON.getStyleName());
        }
    }

    private void handleBotGrabAndEats(int resultX, int resultY, SingleBot bot, ActionResultBundle actionResultBundle) {
        final Food foodCell = (Food) SimulatorContext.getInstance().getCellsArray()[resultX][resultY];
        foodService.removeFood(foodCell);
        this.increaseBotHealth(bot);
        actionResultBundle.addBotActionResult(ActionsResult.CHANGE_TILE, resultX, resultY, Styles.EMPTY.getStyleName());
        if (SimulatorContext.getInstance().getFoodNumber() < Constants.FOOD_NUMBER) {
            Food newFoodCell = (Food) foodService.generateFood();
            actionResultBundle.addBotActionResult(ActionsResult.CHANGE_TILE, newFoodCell.getPositionX(),
                    newFoodCell.getPositionY(), Styles.FOOD.getStyleName());
        }
    }

    private void handleBotEatsAndMove(int resultX, int resultY, SingleBot bot, ActionResultBundle actionResultBundle) {
        final Food foodCell = (Food) SimulatorContext.getInstance().getCellsArray()[resultX][resultY];
        actionResultBundle.addBotActionResult(ActionsResult.REMOVE, bot.getPositionX(), bot.getPositionY(), null);
        actionResultBundle.addBotActionResult(ActionsResult.CHANGE_TEXT, bot.getPositionX(), bot.getPositionY(), "");
        foodService.removeFood(foodCell);
        changeBotPosition(bot, resultX, resultY);
        increaseBotHealth(bot);
        actionResultBundle.addBotActionResult(ActionsResult.CHANGE_TILE, resultX, resultY, Styles.BOT.getStyleName());
        if (SimulatorContext.getInstance().getFoodNumber() < Constants.FOOD_NUMBER) {
            Food newFoodCell = (Food) foodService.generateFood();
            actionResultBundle.addBotActionResult(ActionsResult.CHANGE_TILE, newFoodCell.getPositionX(),
                    newFoodCell.getPositionY(), Styles.FOOD.getStyleName());
        }
    }

    private void increaseBotHealth(final SingleBot bot) {
        final int currentHealth = bot.getHealth();
        final int resultHealth = currentHealth + Constants.FOOD_CALORIES;
        bot.setHealth(Math.min(resultHealth, Constants.BOT_MAX_HEALTH));
    }

    private void handleMoveBot(int resultX, int resultY, SingleBot bot, ActionResultBundle actionResultBundle) {
        actionResultBundle.addBotActionResult(ActionsResult.REMOVE, bot.getPositionX(), bot.getPositionY(), null);
        actionResultBundle.addBotActionResult(ActionsResult.CHANGE_TEXT, bot.getPositionX(), bot.getPositionY(), "");
        changeBotPosition(bot, resultX, resultY);
        actionResultBundle.addBotActionResult(ActionsResult.CHANGE_TILE, resultX, resultY, Styles.BOT.getStyleName());
    }

    private void changeBotPosition(SingleBot bot, int resultX, int resultY) {
        final Cell[][] cellsArray = SimulatorContext.getInstance().getCellsArray();
        cellsArray[bot.getPositionX()][bot.getPositionY()] =
                cellService.createCell(bot.getPositionX(), bot.getPositionY(), CellType.EMPTY);
        bot.setPositionX(resultX);
        bot.setPositionY(resultY);
        cellsArray[resultX][resultY] = bot;
    }
}
