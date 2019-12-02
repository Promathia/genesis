package com.home.genesis.general.services;

import com.home.genesis.Constants;
import com.home.genesis.general.LifeSimulator;
import com.home.genesis.logic.context.SimulatorContext;
import com.home.genesis.logic.entity.ActionResultBundle;
import com.home.genesis.logic.entity.Cell;
import com.home.genesis.logic.entity.SingleBot;
import com.home.genesis.logic.services.BotService;
import com.home.genesis.representation.controllers.WorldViewController;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.util.*;

public class LifeSimulatorService {

    private SimulatorContext simulatorContext;
    private WorldViewController worldViewController;
    private BotService botService;

    public LifeSimulatorService() {
        this.simulatorContext = SimulatorContext.getInstance();
        this.worldViewController = new WorldViewController(simulatorContext.getInitialCells());
        this.botService = new BotService();
    }

    public void drawScene(final Stage stage, final String cssPath) {
        final Pane parent = worldViewController.initializeView();
        final Scene scene = new Scene(parent);
        scene.getStylesheets().add(LifeSimulator.class.getResource(cssPath).toExternalForm());
        stage.setScene(scene);
        stage.show();
    }

    public void startLifeSimulation() {
        final Runnable runnable = new RunSimulation();
        final Thread thread = new Thread(runnable);
        thread.setDaemon(true);
        thread.start();
    }

    private void handleBotActions() throws InterruptedException {
        List<SingleBot> bots = simulatorContext.getBots();
        Cell[][] cellsArray = simulatorContext.getCellsArray();
        ActionResultBundle actionResultBundle = new ActionResultBundle();
        Collections.shuffle(bots);
        for (int botIndex = 0; botIndex < bots.size(); botIndex++) {
            if (bots.size() <= Constants.BOT_MIN_NUMBER) {
                botService.handleNewBotsCreation(actionResultBundle);
                worldViewController.handleActionResults(actionResultBundle);
                break;
            }
            SingleBot singleBot = bots.get(botIndex);
            botService.handleBotDecisionTaking(singleBot, cellsArray, actionResultBundle);
            botService.handleBotDeathOrConsumeCalorie(singleBot, botIndex, actionResultBundle);
            worldViewController.handleActionResults(actionResultBundle);
            Thread.sleep(5);
        }
    }

    private class RunSimulation implements Runnable {
        @Override
        public void run() {
            while (true) {
                try {
                    handleBotActions();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
