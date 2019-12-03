package com.home.genesis.logic.services;

import com.home.genesis.Constants;
import com.home.genesis.logic.CellType;
import com.home.genesis.logic.actions.*;
import com.home.genesis.logic.context.SimulatorContext;
import com.home.genesis.logic.entity.*;
import com.home.genesis.representation.Styles;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Random;

import static com.home.genesis.Constants.DEFAULT_ACTION_NUMBER;

public class BotService {

    private FoodService foodService;
    private PoisonService poisonService;
    private CellService cellService;
    private SimulatorContext simulatorContext;

    public BotService() {
        this.foodService = new FoodService();
        this.cellService = new CellService();
        this.simulatorContext = SimulatorContext.getInstance();
        this.poisonService = new PoisonService();
    }

    public void handleBotDecisionTaking(final SingleBot bot, final Cell[][] cellsArray, final ActionResultBundle actionResultBundle) {
        final int currentAction = bot.getDnaCommands().get(bot.getCurrentStep());
        int actionPointerMoveTo = 0;
        if (currentAction >= (DEFAULT_ACTION_NUMBER * 4)) {
            actionPointerMoveTo = currentAction; //from 32 to 63 we just move the pointer to this number
        } else if (currentAction >= (DEFAULT_ACTION_NUMBER * 3)) {
            // we decrease in order to access enum by ordinal
            final int valueModifiedByCurrentDirection = updateWithCurrentDirection(currentAction - DEFAULT_ACTION_NUMBER * 3, bot.getDirection());
            TurnActionType turnActionType = TurnActionType.values()[valueModifiedByCurrentDirection];
            actionPointerMoveTo = handleTurnActionResult(turnActionType, bot, actionResultBundle);
        } else if (currentAction >= (DEFAULT_ACTION_NUMBER * 2)) {
            final int valueModifiedByCurrentDirection = updateWithCurrentDirection(currentAction - DEFAULT_ACTION_NUMBER * 2, bot.getDirection());
            CheckActionType checkActionType = CheckActionType.values()[valueModifiedByCurrentDirection];
            actionPointerMoveTo = handleCheckActionResult(checkActionType, bot, cellsArray);
        } else if (currentAction >= DEFAULT_ACTION_NUMBER) {
            final int valueModifiedByCurrentDirection = updateWithCurrentDirection(currentAction - DEFAULT_ACTION_NUMBER, bot.getDirection());
            GrabActionType grabActionType = GrabActionType.values()[valueModifiedByCurrentDirection];
            actionPointerMoveTo = handleGrabActionResult(grabActionType, bot, cellsArray, actionResultBundle);
        } else {
            final int valueModifiedByCurrentDirection = updateWithCurrentDirection(currentAction, bot.getDirection());
            MoveActionType moveActionType = MoveActionType.values()[valueModifiedByCurrentDirection];
            actionPointerMoveTo = handleMoveActionResult(moveActionType, bot, cellsArray, actionResultBundle);
        }
        updateBotActionPointer(bot, actionPointerMoveTo);
    }

    public void handleBotDeathOrConsumeCalorie(SingleBot singleBot, ListIterator<SingleBot> singleBotListIterator, ActionResultBundle actionResultBundle) {
        if (singleBot.getHealth() > 1) {
            decreaseBotHealth(singleBot, actionResultBundle);
        } else {
            final int positionX = singleBot.getPositionX();
            final int positionY = singleBot.getPositionY();
            Cell cell = cellService.createCell(positionX, positionY, CellType.EMPTY);
            simulatorContext.getCellsArray()[positionX][positionY] = cell;
            singleBotListIterator.remove();
            actionResultBundle.addActionResult(ActionsResult.CHANGE_TILE, positionX, positionY, Styles.EMPTY.getStyleName());
            actionResultBundle.addActionResult(ActionsResult.CHANGE_TEXT, positionX, positionY, "");
        }
    }

    public void handleNewBotsCreation(ListIterator<SingleBot> singleBotListIterator, ActionResultBundle actionResultBundle) {
        List<SingleBot> bots = new ArrayList<>(simulatorContext.getBots());
        int genomeModificationCounter = 0;
        for (SingleBot bot : bots) {
            if (genomeModificationCounter < Constants.MAX_MUTATIONS_PER_GENERATION && tryToModifyGenome(bot)) {
                genomeModificationCounter++;
            }
            copyBots(bot, singleBotListIterator, actionResultBundle);
        }
    }

    private void copyBots(final SingleBot bot, ListIterator<SingleBot> singleBotListIterator, final ActionResultBundle actionResultBundle) {
        // we subtract 1 as we currently have bots, 64max / 8min
        // equal 8, but each bot should produce 7 copies
        final int copiesNumber = (Constants.BOT_MAX_NUMBER / Constants.BOT_MIN_NUMBER) - 1;
        final Cell[][] cellsArray = simulatorContext.getCellsArray();
        for (int i = 0; i < copiesNumber; i++) {
            final Cell cell = cellService.getRandomEmptyCell();
            final int positionX = cell.getPositionX();
            final int positionY = cell.getPositionY();
            SingleBot copiedBot = new SingleBot(bot.getDnaCommands(), positionX, positionY);
            cellsArray[positionX][positionY] = copiedBot;
            singleBotListIterator.add(copiedBot);
            actionResultBundle.addActionResult(ActionsResult.CHANGE_TILE, positionX, positionY, Styles.BOT.getStyleName());
            actionResultBundle.addActionResult(ActionsResult.CHANGE_TEXT, positionX, positionY, concatHealthAndDirection(copiedBot));
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
        actionResultBundle.addActionResult(ActionsResult.CHANGE_TEXT, bot.getPositionX(), bot.getPositionY(), concatHealthAndDirection(bot));
    }

    private int handleTurnActionResult(final TurnActionType turnActionType, final SingleBot bot, final ActionResultBundle actionResultBundle) {
        bot.setDirection(turnActionType.getTurnDirection());
        actionResultBundle.addActionResult(ActionsResult.CHANGE_TEXT, bot.getPositionX(), bot.getPositionY(), concatHealthAndDirection(bot));
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
        int intermediateResult = actionNumber + turnNumber;
        if (intermediateResult > 7) {
            return intermediateResult - 7;
        } else {
            return intermediateResult;
        }
    }

    private int handleCheckActionResult(CheckActionType checkActionType, SingleBot bot, Cell[][] cellsArray) {
        final int resultX = bot.getPositionX() + checkActionType.getX();
        final int resultY = bot.getPositionY() + checkActionType.getY();
        final Cell destinationCell = cellsArray[resultX][resultY];
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

    private int handleGrabActionResult(GrabActionType grabActionType, SingleBot bot, Cell[][] cellsArray, ActionResultBundle actionResultBundle) {
        final int resultX = bot.getPositionX() + grabActionType.getX();
        final int resultY = bot.getPositionY() + grabActionType.getY();
        final Cell destinationCell = cellsArray[resultX][resultY];
        final CellType cellType = destinationCell.getCellType();
        if (cellType == CellType.POISON) {
            handleNeutralizePoison(resultX, resultY, cellsArray, actionResultBundle);
            return 1;
        } else if (cellType == CellType.OBSTACLE) {
            return 2;
        } else if (cellType == CellType.BOT) {
            return 3;
        } else if (cellType == CellType.FOOD) {
            handleBotGrabAndEats(resultX, resultY, bot, cellsArray, actionResultBundle);
            return 4;
        } else {
            return 5;
        }
    }

    private void handleNeutralizePoison(int resultX, int resultY, Cell[][] cellsArray, ActionResultBundle actionResultBundle) {
        Poison poisonCell = (Poison) cellsArray[resultX][resultY];
        poisonService.removePoison(poisonCell);
        Food food = (Food) cellService.createCell(resultX, resultY, CellType.FOOD);
        foodService.addFood(food);
        actionResultBundle.addActionResult(ActionsResult.CHANGE_TILE, resultX, resultY, Styles.FOOD.getStyleName());
        if (simulatorContext.getPoison().size() < Constants.POISON_NUMBER) {
            Cell newPoisonCell = poisonService.generatePoison();
            actionResultBundle.addActionResult(ActionsResult.CHANGE_TILE, newPoisonCell.getPositionX(), newPoisonCell.getPositionY(), Styles.POISON.getStyleName());
        }
    }

    private int handleMoveActionResult(final MoveActionType moveActionType,
                                       final SingleBot bot,
                                       final Cell[][] cellsArray,
                                       final ActionResultBundle actionResultBundle) {
        final int resultX = bot.getPositionX() + moveActionType.getX();
        final int resultY = bot.getPositionY() + moveActionType.getY();
        final Cell destinationCell = cellsArray[resultX][resultY];
        final CellType cellType = destinationCell.getCellType();
        if (cellType == CellType.POISON) {
            handleBotDies(resultX, resultY, bot, cellsArray, actionResultBundle);
            return 1;
        } else if (cellType == CellType.OBSTACLE) {
            return 2;
        } else if (cellType == CellType.BOT) {
            return 3;
        } else if (cellType == CellType.FOOD) {
            handleBotEatsAndMove(resultX, resultY, bot, cellsArray, actionResultBundle);
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

    private void handleBotDies(int resultX, int resultY, SingleBot bot, Cell[][] cellsArray, ActionResultBundle actionResultBundle) {
        final Poison poisonCell = (Poison) cellsArray[resultX][resultY];
        bot.setHealth(-1);
        poisonService.removePoison(poisonCell);
        actionResultBundle.addActionResult(ActionsResult.REMOVE, bot.getPositionX(), bot.getPositionY(), null);
        actionResultBundle.addActionResult(ActionsResult.CHANGE_TEXT, bot.getPositionX(), bot.getPositionY(), "");
        actionResultBundle.addActionResult(ActionsResult.REMOVE, resultX, resultY, null);
        if (simulatorContext.getPoison().size() < Constants.POISON_NUMBER) {
            Poison newPoisonCell = (Poison) poisonService.generatePoison();
            actionResultBundle.addActionResult(ActionsResult.CHANGE_TILE, newPoisonCell.getPositionX(),
                    newPoisonCell.getPositionY(), Styles.POISON.getStyleName());
        }
    }

    private void handleBotGrabAndEats(int resultX, int resultY, SingleBot bot, Cell[][] cellsArray, ActionResultBundle actionResultBundle) {
        final Food foodCell = (Food) cellsArray[resultX][resultY];
        foodService.removeFood(foodCell);
        this.increaseBotHealth(bot);
        actionResultBundle.addActionResult(ActionsResult.CHANGE_TILE, resultX, resultY, Styles.EMPTY.getStyleName());
        //we dont push health event as it is decreased generally
        if (simulatorContext.getFood().size() < Constants.FOOD_NUMBER) {
            Food newFoodCell = (Food) foodService.generateFood();
            actionResultBundle.addActionResult(ActionsResult.CHANGE_TILE, newFoodCell.getPositionX(),
                    newFoodCell.getPositionY(), Styles.FOOD.getStyleName());
        }
    }

    private void handleBotEatsAndMove(int resultX, int resultY, SingleBot bot, Cell[][] cellsArray, ActionResultBundle actionResultBundle) {
        final Food foodCell = (Food) cellsArray[resultX][resultY];
        actionResultBundle.addActionResult(ActionsResult.REMOVE, bot.getPositionX(), bot.getPositionY(), null);
        actionResultBundle.addActionResult(ActionsResult.CHANGE_TEXT, bot.getPositionX(), bot.getPositionY(), "");
        foodService.removeFood(foodCell);
        changeBotPosition(bot, resultX, resultY);
        increaseBotHealth(bot);
        actionResultBundle.addActionResult(ActionsResult.CHANGE_TILE, resultX, resultY, Styles.BOT.getStyleName());
        //TODO not required as health is decreased generally
        if (simulatorContext.getFood().size() < Constants.FOOD_NUMBER) {
            Food newFoodCell = (Food) foodService.generateFood();
            actionResultBundle.addActionResult(ActionsResult.CHANGE_TILE, newFoodCell.getPositionX(),
                    newFoodCell.getPositionY(), Styles.FOOD.getStyleName());
        }
    }

    private void increaseBotHealth(final SingleBot bot) {
        final int currentHealth = bot.getHealth();
        final int resultHealth = currentHealth + Constants.FOOD_CALORIES;
        bot.setHealth(Math.min(resultHealth, Constants.BOT_MAX_HEALTH));
    }

    private void handleMoveBot(int resultX, int resultY, SingleBot bot, ActionResultBundle actionResultBundle) {
        actionResultBundle.addActionResult(ActionsResult.REMOVE, bot.getPositionX(), bot.getPositionY(), null);
        actionResultBundle.addActionResult(ActionsResult.CHANGE_TEXT, bot.getPositionX(), bot.getPositionY(), "");
        changeBotPosition(bot, resultX, resultY);
        actionResultBundle.addActionResult(ActionsResult.CHANGE_TILE, resultX, resultY, Styles.BOT.getStyleName());
    }

    private void changeBotPosition(SingleBot bot, int resultX, int resultY) {
        final Cell[][] cellsArray = simulatorContext.getCellsArray();
        cellsArray[bot.getPositionX()][bot.getPositionY()] =
                cellService.createCell(bot.getPositionX(), bot.getPositionY(), CellType.EMPTY);
        bot.setPositionX(resultX);
        bot.setPositionY(resultY);
        cellsArray[resultX][resultY] = bot;
    }
}
