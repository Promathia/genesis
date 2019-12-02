package com.home.genesis.logic.services;

import com.home.genesis.Constants;
import com.home.genesis.logic.CellType;
import com.home.genesis.logic.actions.*;
import com.home.genesis.logic.context.SimulatorContext;
import com.home.genesis.logic.entity.ActionResultBundle;
import com.home.genesis.logic.entity.Cell;
import com.home.genesis.logic.entity.SingleBot;
import com.home.genesis.representation.Styles;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

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
        if (currentAction < DEFAULT_ACTION_NUMBER) {
            MoveActionType moveActionType = MoveActionType.values()[currentAction];
            actionPointerMoveTo = handleMoveActionResult(moveActionType, bot, cellsArray, actionResultBundle);
        } else if (currentAction < (DEFAULT_ACTION_NUMBER * 2)) {
            // we decrease in order to access enum by ordinal
            GrabActionType grabActionType = GrabActionType.values()[currentAction - DEFAULT_ACTION_NUMBER];
            actionPointerMoveTo = handleGrabActionResult(grabActionType, bot, cellsArray, actionResultBundle);
        } else if (currentAction < (DEFAULT_ACTION_NUMBER * 3)) {
            CheckActionType checkActionType = CheckActionType.values()[currentAction - DEFAULT_ACTION_NUMBER * 2];
            actionPointerMoveTo = handleCheckActionResult(checkActionType, bot, cellsArray);
        } else if (currentAction < (DEFAULT_ACTION_NUMBER * 4)) {
            TurnActionType turnActionType = TurnActionType.values()[currentAction - DEFAULT_ACTION_NUMBER * 3];
            actionPointerMoveTo = handleTurnActionResult(turnActionType, bot, actionResultBundle);
        } else {
            actionPointerMoveTo = currentAction; //from 32 to 63 we just move the pointer to this number
        }
        updateBotActionPointer(bot, actionPointerMoveTo);
    }

    public void handleBotDeathOrConsumeCalorie(SingleBot singleBot, int botIndex, ActionResultBundle actionResultBundle) {
        if (singleBot.getHealth() > 1) {
            decreaseBotHealth(singleBot, actionResultBundle);
        } else {
            simulatorContext.getBots().remove(botIndex);
            actionResultBundle.addActionResult(ActionsResult.CHANGE_TILE, singleBot.getPositionX(), singleBot.getPositionY(), Styles.EMPTY.getStyleName());
            actionResultBundle.addActionResult(ActionsResult.CHANGE_TEXT, singleBot.getPositionX(), singleBot.getPositionY(), "");
        }
    }

    public void handleNewBotsCreation(ActionResultBundle actionResultBundle) {
        List<SingleBot> bots = new ArrayList<>(simulatorContext.getBots());
        int genomeModificationCounter = 0;
        for (SingleBot bot : bots) {
            if (genomeModificationCounter < Constants.MAX_MUTATIONS_PER_GENERATION && tryToModifyGenome(bot)) {
                genomeModificationCounter++;
            }
            copyBots(bot, actionResultBundle);
        }
    }

    private void copyBots(final SingleBot bot, final ActionResultBundle actionResultBundle) {
        final int copiesNumber = Constants.BOT_MAX_NUMBER / Constants.BOT_MIN_NUMBER; //TODO check evenness
        List<SingleBot> bots = simulatorContext.getBots();
        Cell[][] cellsArray = simulatorContext.getCellsArray();
        for (int i = 0; i < copiesNumber; i++) {
            Cell cell = cellService.getEmptyCell();
            int positionX = cell.getPositionX();
            int positionY = cell.getPositionY();
            SingleBot copiedBot = new SingleBot(bot.getDnaCommands(), cell.getPositionX(), cell.getPositionY());
            cellsArray[positionX][positionY] = copiedBot;
            bots.add(copiedBot);
            actionResultBundle.addActionResult(ActionsResult.CHANGE_TILE, positionX, positionY, Styles.BOT.getStyleName());
            actionResultBundle.addActionResult(ActionsResult.CHANGE_TEXT, positionX, positionY, String.valueOf(copiedBot.getHealth()));
        }
    }

    private boolean tryToModifyGenome(final SingleBot bot) {
        final ThreadLocalRandom random = ThreadLocalRandom.current();
        final boolean doChange = random.nextBoolean();
        if (doChange) {
            final int commandNumber = random.nextInt(Constants.BOT_DNA_COMMANDS);
            final int commandValue = random.nextInt(Constants.BOT_DNA_COMMANDS);
            bot.getDnaCommands().add(commandNumber, commandValue);
        }
        return doChange;
    }

    private void decreaseBotHealth(final SingleBot bot, final ActionResultBundle actionResultBundle) {
        bot.setHealth(bot.getHealth() - Constants.BOT_CONSUMES_CALORIES);
        actionResultBundle.addActionResult(ActionsResult.CHANGE_TEXT, bot.getPositionX(), bot.getPositionY(), String.valueOf(bot.getHealth()));
    }

    private int handleTurnActionResult(final TurnActionType turnActionType, final SingleBot bot, final ActionResultBundle actionResultBundle) {
        bot.setDirection(turnActionType.getTurnDirection());
        actionResultBundle.addActionResult(ActionsResult.BOT_TURN, bot.getPositionX(), bot.getPositionY(), String.valueOf(turnActionType.getTurnDirection()));
        return 1;
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
        final Cell poisonCell = cellsArray[resultX][resultY];
        poisonService.convertPoisonToFood(poisonCell);
        Cell newPoisonCell = poisonService.generatePoison();
        actionResultBundle.addActionResult(ActionsResult.CHANGE_TILE, resultX, resultY, Styles.FOOD.getStyleName());
        actionResultBundle.addActionResult(ActionsResult.CHANGE_TILE, newPoisonCell.getPositionX(), newPoisonCell.getPositionY(), Styles.POISON.getStyleName());
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
            handleMoveBot(resultX, resultY, bot, cellsArray, actionResultBundle);
            return 5;
        }
    }

    private void updateBotActionPointer(SingleBot bot, int actionPointerMoveTo) {
        final int currentPointerPosition = bot.getCurrentStep();
        final int newPointerPosition = currentPointerPosition + actionPointerMoveTo;
        if (newPointerPosition >= Constants.BOT_DNA_COMMANDS) {
            bot.setCurrentStep(newPointerPosition % Constants.BOT_DNA_COMMANDS);
        } else {
            bot.setCurrentStep(newPointerPosition);
        }
    }

    private void handleBotDies(int resultX, int resultY, SingleBot bot, Cell[][] cellsArray, ActionResultBundle actionResultBundle) {
        final Cell poisonCell = cellsArray[resultX][resultY];
        bot.setHealth(-1);
        poisonService.removePoison(poisonCell);
        Cell newPoisonCell = poisonService.generatePoison();
        actionResultBundle.addActionResult(ActionsResult.REMOVE, bot.getPositionX(), bot.getPositionY(), null);
        actionResultBundle.addActionResult(ActionsResult.CHANGE_TEXT, bot.getPositionX(), bot.getPositionY(), "");
        actionResultBundle.addActionResult(ActionsResult.REMOVE, resultX, resultY, null);
        actionResultBundle.addActionResult(ActionsResult.CHANGE_TILE, newPoisonCell.getPositionX(),
                newPoisonCell.getPositionY(), Styles.POISON.getStyleName());
    }

    /*private void removeBot(SingleBot bot, Cell[][] cellsArray) {
        simulatorContext.getBots().remove(bot);
        cellsArray[bot.getPositionX()][bot.getPositionY()] = cellService.createCell(bot.getPositionX(), bot.getPositionY(), CellType.EMPTY);
    }*/

    private void handleBotGrabAndEats(int resultX, int resultY, SingleBot bot, Cell[][] cellsArray, ActionResultBundle actionResultBundle) {
        final Cell foodCell = cellsArray[resultX][resultY];
        foodService.removeFood(foodCell);
        Cell newFoodCell = foodService.generateFood();
        increaseBotHealth(bot);
        actionResultBundle.addActionResult(ActionsResult.CHANGE_TILE, resultX, resultY, Styles.EMPTY.getStyleName());
        //actionResultBundle.addActionResult(ActionsResult.CHANGE_TEXT, resultX, resultY, String.valueOf(bot.getHealth())); //TODO same to do for health
        actionResultBundle.addActionResult(ActionsResult.CHANGE_TILE, newFoodCell.getPositionX(),
                newFoodCell.getPositionY(), Styles.FOOD.getStyleName());
    }

    private void handleBotEatsAndMove(int resultX, int resultY, SingleBot bot, Cell[][] cellsArray, ActionResultBundle actionResultBundle) {
        final Cell foodCell = cellsArray[resultX][resultY];
        actionResultBundle.addActionResult(ActionsResult.REMOVE, bot.getPositionX(), bot.getPositionY(), null);
        changeBotPosition(bot, resultX, resultY, cellsArray);
        foodService.removeFood(foodCell);
        Cell newFoodCell = foodService.generateFood();
        increaseBotHealth(bot);
        actionResultBundle.addActionResult(ActionsResult.CHANGE_TILE, resultX, resultY, Styles.BOT.getStyleName());
        //actionResultBundle.addActionResult(ActionsResult.CHANGE_TEXT, resultX, resultY, String.valueOf(bot.getHealth())); //TODO not required as health is decreased generally
        actionResultBundle.addActionResult(ActionsResult.CHANGE_TILE, newFoodCell.getPositionX(),
                newFoodCell.getPositionY(), Styles.FOOD.getStyleName());
    }

    private void increaseBotHealth(final SingleBot bot) {
        final int currentHealth = bot.getHealth();
        final int resultHealth = currentHealth + Constants.FOOD_CALORIES;
        bot.setHealth(Math.min(resultHealth, Constants.BOT_MAX_HEALTH));
    }

    private void handleMoveBot(int resultX, int resultY, SingleBot bot, Cell[][] cellsArray, ActionResultBundle actionResultBundle) {
        actionResultBundle.addActionResult(ActionsResult.REMOVE, bot.getPositionX(), bot.getPositionY(), null);
        changeBotPosition(bot, resultX, resultY, cellsArray);
        actionResultBundle.addActionResult(ActionsResult.CHANGE_TILE, resultX, resultY, Styles.BOT.getStyleName());
    }

    private void changeBotPosition(SingleBot bot, int resultX, int resultY, Cell[][] cellsArray) {
        cellsArray[bot.getPositionX()][bot.getPositionY()] =
                cellService.createCell(bot.getPositionX(), bot.getPositionY(), CellType.EMPTY);
        bot.setPositionX(resultX);
        bot.setPositionY(resultY);
        cellsArray[resultX][resultY] = bot;
    }
}
