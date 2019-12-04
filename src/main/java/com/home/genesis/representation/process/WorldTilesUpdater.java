package com.home.genesis.representation.process;

import com.home.genesis.logic.actions.ActionsResult;
import com.home.genesis.logic.entity.results.ActionResultBundle;
import com.home.genesis.logic.entity.results.BotResults;
import com.home.genesis.representation.Styles;
import com.home.genesis.representation.entity.WorldTile;

import java.util.List;

public class WorldTilesUpdater implements Runnable {

    private ActionResultBundle actionResultBundle;
    private WorldTile[][] tilesArray;

    public WorldTilesUpdater(final WorldTile[][] tilesArray, final ActionResultBundle actionResultBundle) {
        this.actionResultBundle = actionResultBundle;
        this.tilesArray = tilesArray;
    }

    @Override
    public void run() {
        List<BotResults> botResults = actionResultBundle.getBotResults();
        for (BotResults botResult : botResults) {
            ActionsResult actionsResult = botResult.getActionsResult();
            switch (actionsResult) {
                case REMOVE:
                    WorldTile removeTile = tilesArray[botResult.getX()][botResult.getY()];
                    removeTile.getTileBackground().getStyleClass().clear();
                    removeTile.getTileBackground().getStyleClass().add(Styles.EMPTY.getStyleName());
                    break;
                case CHANGE_TILE:
                    WorldTile changeTile = tilesArray[botResult.getX()][botResult.getY()];
                    changeTile.getTileBackground().getStyleClass().clear();
                    changeTile.getTileBackground().getStyleClass().add(botResult.getPayload());
                    break;
                case CHANGE_TEXT:
                    WorldTile changeTextTile = tilesArray[botResult.getX()][botResult.getY()];
                    changeTextTile.getText().setText(botResult.getPayload());
                    break;
                default:
                    System.out.println("Wrong UI update action!");
            }
        }
    }
}
