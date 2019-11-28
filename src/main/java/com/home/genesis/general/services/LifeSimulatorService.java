package com.home.genesis.general.services;

import com.home.genesis.general.LifeSimulator;
import com.home.genesis.logic.entity.Cell;
import com.home.genesis.logic.services.CellService;
import com.home.genesis.representation.controllers.WorldViewController;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.util.Set;

public class LifeSimulatorService {

    public void drawScene(final Stage stage, final String cssPath) {
        final CellService cellService = new CellService();
        final Set<Cell> initialCells = cellService.getInitialCells();
        final WorldViewController worldViewController = new WorldViewController();
        final Pane parent = worldViewController.initializeView(initialCells);
        final Scene scene = new Scene(parent);
        scene.getStylesheets().add(LifeSimulator.class.getResource(cssPath).toExternalForm());
        stage.setScene(scene);
        stage.show();
    }

}
