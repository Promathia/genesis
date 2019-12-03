package com.home.genesis.general;

import com.home.genesis.general.services.LifeSimulatorService;
import javafx.application.Application;
import javafx.stage.Stage;

public final class LifeSimulator extends Application {

    public static void main(final String[] args) {
        launch(args);
    }

    @Override
    public void start(final Stage stage) {
        final LifeSimulatorService lifeSimulatorService = new LifeSimulatorService();
        lifeSimulatorService.drawScene(stage, "/styles.css");
        lifeSimulatorService.startLifeSimulationProcess();
    }

}
