package com.home.genesis.general.services;

import com.home.genesis.general.LifeSimulator;
import com.home.genesis.logic.CellType;
import com.home.genesis.logic.actions.ActionsResult;
import com.home.genesis.logic.actions.GrabActionType;
import com.home.genesis.logic.actions.MoveActionType;
import com.home.genesis.logic.context.SimulatorContext;
import com.home.genesis.logic.entity.ActionResultBundle;
import com.home.genesis.logic.entity.Cell;
import com.home.genesis.logic.entity.SingleBot;
import com.home.genesis.representation.controllers.WorldViewController;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.util.*;

public class LifeSimulatorService {

    private final SimulatorContext simulatorContext = SimulatorContext.getInstance();
    private final WorldViewController worldViewController = new WorldViewController();

    public void drawScene(final Stage stage, final String cssPath) {
        final Set<Cell> initialCells = simulatorContext.getInitialCells();
        final Pane parent = worldViewController.initializeView(initialCells);
        final Scene scene = new Scene(parent);
        scene.getStylesheets().add(LifeSimulator.class.getResource(cssPath).toExternalForm());
        stage.setScene(scene);
        stage.show();
    }

    public void startLifeSimulation() {
        final Runnable runnable = new RunSimulation();
        final Thread thread = new Thread(runnable);
        thread.setDaemon(true);
        //thread.start();
    }

    private void handleBotActions() {
        Set<SingleBot> bots = simulatorContext.getBots();
        Cell[][] cellsArray = simulatorContext.getCellsArray();
        for (SingleBot bot : bots) {
            //int positionX = bot.getPositionX();
            //int positionY = bot.getPositionY();
            int currentStep = bot.getCurrentStep();
            int currentAction = bot.getDnaCommands().get(currentStep);
            int movePointerToPosition = doTakeDecision(currentAction, bot, cellsArray);
        }
    }

    private int doTakeDecision(final int currentAction, SingleBot bot, Cell[][] cellsArray) {
        int movePointerToPosition = 0;
        if (currentAction < 8) {
            MoveActionType moveActionType = MoveActionType.values()[currentAction];
            int x = moveActionType.getX();
            int y = moveActionType.getY();
            ActionsResult actionsResult = getMoveActionResult(x, y, bot, cellsArray);

        } else if (currentAction < 16) {
            GrabActionType grabActionType = GrabActionType.values()[currentAction];
        } else if (currentAction < 24) {
            MoveActionType moveActionType = MoveActionType.values()[currentAction];
        } else {

        }
        return movePointerToPosition;
    }

    private void handleActionResult(SingleBot bot, ActionsResult actionsResult) {
        ActionResultBundle actionResultBundle = new ActionResultBundle();
        if (actionsResult.equals(ActionsResult.BOT_DIES)) {

        }
    }

    private ActionsResult getMoveActionResult(int x, int y, SingleBot bot, Cell[][] cellsArray) {
        int resultX = bot.getPositionX() + x;
        int resultY = bot.getPositionY() + y;
        Cell cell = cellsArray[resultX][resultY];
        CellType cellType = cell.getCellType();
        if (cellType == CellType.BOT ||
            cellType == CellType.OBSTACLE) {
            return ActionsResult.BOT_DOES_NOTHING;
        } else if (cellType == CellType.FOOD) {
            return ActionsResult.BOT_EATS;
        } else if (cellType == CellType.POISON) {
            return ActionsResult.BOT_DIES;
        } else {
            return ActionsResult.BOT_MOVES;
        }
    }

    private class RunSimulation implements Runnable {
        @Override
        public void run() {
            while(true) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                handleBotActions();
            }
        }
    }

}
