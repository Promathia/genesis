package com.home.genesis.general.services;

import com.home.genesis.Constants;
import com.home.genesis.general.LifeSimulator;
import com.home.genesis.general.process.RunSimulation;
import com.home.genesis.general.process.UpdateUI;
import com.home.genesis.logic.context.SimulatorContext;
import com.home.genesis.logic.entity.ActionResultBundle;
import com.home.genesis.representation.controllers.WorldViewController;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class LifeSimulatorService {

    private WorldViewController worldViewController;
    private int pauseTimeBetweenActions = Constants.DEFAULT_PAUSE_BETWEEN_BOT_ACTIONS;

    private final BlockingQueue<ActionResultBundle> resultsQueue = new LinkedBlockingQueue<>(Constants.UPDATE_QUEUE_CAPACITY);

    public LifeSimulatorService() {
        this.worldViewController = new WorldViewController(SimulatorContext.getInstance().getInitialCells(), pauseTimeBetweenActions);
    }

    public void drawScene(final Stage stage, final String cssPath) {
        final Pane parent = worldViewController.initializeView();
        final Scene scene = new Scene(parent);
        scene.getStylesheets().add(LifeSimulator.class.getResource(cssPath).toExternalForm());
        stage.setTitle("Life Simulation");
        stage.setScene(scene);
        stage.show();
    }

    public void startLifeSimulationProcess() {
        final Thread logicThread = new Thread(new RunSimulation(resultsQueue, this));
        logicThread.setDaemon(true);
        logicThread.start();
        final Thread uiThread = new Thread(new UpdateUI(resultsQueue, worldViewController));
        uiThread.setDaemon(true);
        uiThread.start();
    }

    public int getPauseTimeBetweenActions() {
        return worldViewController.getCurrentActionPauseValue();
    }
}
