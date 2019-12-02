package com.home.genesis.representation.controllers;

import com.home.genesis.logic.actions.ActionsResult;
import com.home.genesis.logic.entity.ActionResultBundle;
import com.home.genesis.logic.entity.Cell;
import com.home.genesis.logic.entity.SingleBot;
import com.home.genesis.representation.entity.Tile;
import com.home.genesis.representation.service.WorldControllerService;
import javafx.scene.layout.Pane;

import java.util.List;
import java.util.Set;

import static com.home.genesis.Constants.TILE_SIZE;

public class WorldViewController {

    private WorldControllerService worldControllerService;

    private Tile[][] tilesArray;

    public WorldViewController(final List<Cell> initialCells) {
        this.worldControllerService = new WorldControllerService();
        this.tilesArray = worldControllerService.getTiles(initialCells);
    }

    public Pane initializeView() {
        if (tilesArray == null || tilesArray.length == 0) {
            System.out.println("No tiles created!");
            return new Pane();
        }
        return worldControllerService.initializeView(tilesArray);
    }

    public void handleActionResults(ActionResultBundle actionResultBundle) {
        List<ActionResultBundle.Result> results = actionResultBundle.getResults();
        for (ActionResultBundle.Result result : results) {
            Tile tile = tilesArray[result.getX()][result.getY()];
            ActionsResult actionsResult = result.getActionsResult();
            switch (actionsResult) {
                case REMOVE:
                    tile.getTileBackground().getStyleClass().clear();
                    tile.getTileBackground().getStyleClass().add("empty-tile");
                    tile.getText().setText("");
                    break;
                case CHANGE_TILE:
                    tile.getTileBackground().getStyleClass().clear();
                    tile.getTileBackground().getStyleClass().add(result.getPayload());
                    break;
                case CHANGE_TEXT:
                    tile.getText().setText(result.getPayload());
                    break;
                case BOT_TURN:
                    break;
                default:
            }
        }
        results.clear();
    }
}
