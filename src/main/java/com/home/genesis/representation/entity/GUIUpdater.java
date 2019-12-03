package com.home.genesis.representation.entity;

import com.home.genesis.logic.actions.ActionsResult;
import com.home.genesis.logic.entity.ActionResultBundle;
import com.home.genesis.representation.Styles;

import java.util.List;

public class GUIUpdater implements Runnable {

    private ActionResultBundle actionResultBundle;
    private WorldTile[][] tilesArray;

    public GUIUpdater(final WorldTile[][] tilesArray, final ActionResultBundle actionResultBundle) {
        this.actionResultBundle = actionResultBundle;
        this.tilesArray = tilesArray;
    }

    @Override
    public void run() {
        List<ActionResultBundle.Result> results = actionResultBundle.getResults();
        for (ActionResultBundle.Result result : results) {
            ActionsResult actionsResult = result.getActionsResult();
            switch (actionsResult) {
                case REMOVE:
                    WorldTile removeTile = tilesArray[result.getX()][result.getY()];
                    removeTile.getTileBackground().getStyleClass().clear();
                    removeTile.getTileBackground().getStyleClass().add(Styles.EMPTY.getStyleName());
                    break;
                case CHANGE_TILE:
                    WorldTile changeTile = tilesArray[result.getX()][result.getY()];
                    changeTile.getTileBackground().getStyleClass().clear();
                    changeTile.getTileBackground().getStyleClass().add(result.getPayload());
                    break;
                case CHANGE_TEXT:
                    WorldTile changeTextTile = tilesArray[result.getX()][result.getY()];
                    changeTextTile.getText().setText(result.getPayload());
                    break;
                default:
                    System.out.println("Wrong UI update action!");
            }
        }
    }
}
