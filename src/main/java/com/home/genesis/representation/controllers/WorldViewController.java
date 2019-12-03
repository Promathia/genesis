package com.home.genesis.representation.controllers;

import com.home.genesis.logic.entity.ActionResultBundle;
import com.home.genesis.logic.entity.Cell;
import com.home.genesis.representation.entity.GUIUpdater;
import com.home.genesis.representation.entity.WorldTile;
import com.home.genesis.representation.service.WorldControllerService;
import javafx.application.Platform;
import javafx.scene.layout.Pane;

import java.util.List;

public class WorldViewController {

    private WorldControllerService worldControllerService;

    private WorldTile[][] tilesArray;

    private Pane mainPane;

    public WorldViewController(final List<Cell> initialCells, final int currentActionPauseValue) {
        this.worldControllerService = new WorldControllerService(currentActionPauseValue);
        this.tilesArray = worldControllerService.getTiles(initialCells);
    }

    public Pane initializeView() {
        if (tilesArray == null || tilesArray.length == 0) {
            System.out.println("No tiles created!");
            mainPane = new Pane();
            return mainPane;
        }
        mainPane = worldControllerService.initializeView(tilesArray);
        return mainPane;
    }

    public void handleActionResults(ActionResultBundle actionResultBundle) {
        Platform.runLater(new GUIUpdater(tilesArray, actionResultBundle));
    }

    public int getCurrentActionPauseValue() {
        return worldControllerService.getCurrentActionPauseValue();
    }
}
