package com.home.genesis.representation.controllers;

import com.home.genesis.logic.entity.results.ActionResultBundle;
import com.home.genesis.logic.entity.Cell;
import com.home.genesis.representation.entity.GenomeTile;
import com.home.genesis.representation.process.StatsViewUpdater;
import com.home.genesis.representation.process.WorldTilesUpdater;
import com.home.genesis.representation.entity.WorldTile;
import com.home.genesis.representation.service.WorldDataService;
import com.home.genesis.representation.service.WorldViewService;
import javafx.application.Platform;
import javafx.scene.layout.Pane;

import java.util.List;
import java.util.concurrent.Semaphore;

public class WorldViewController {

    private WorldViewService worldViewService;
    private WorldDataService worldDataService;

    private WorldTile[][] tilesArray;
    private GenomeTile[][] genomeTiles;

    private Pane mainPane;

    public WorldViewController(final List<Cell> initialCells,
                               final List<Integer> genomeData,
                               final Semaphore semaphore,
                               final int currentActionPauseValue) {
        this.worldViewService = new WorldViewService(currentActionPauseValue, semaphore);
        this.worldDataService = new WorldDataService();
        this.tilesArray = worldDataService.getBotTiles(initialCells);
        this.genomeTiles = worldDataService.getGenomeTiles(genomeData);
    }

    public Pane initializeView() {
        if (tilesArray == null || tilesArray.length == 0 || genomeTiles == null || genomeTiles.length == 0) {
            System.out.println("No tiles or genomes created!");
            mainPane = new Pane();
            return mainPane;
        }
        mainPane = worldViewService.initializeView(tilesArray, genomeTiles);
        return mainPane;
    }

    public void handleActionResults(ActionResultBundle actionResultBundle) {
        if (actionResultBundle.getBotResults().size() > 0) {
            Platform.runLater(new WorldTilesUpdater(tilesArray, actionResultBundle));
        }
        if (actionResultBundle.getGenomeResults().size() > 0) {
            Platform.runLater(new StatsViewUpdater(genomeTiles, mainPane, actionResultBundle));
        }
    }

    public int getCurrentActionPauseValue() {
        return worldViewService.getCurrentActionPauseValue();
    }
}
