package com.home.genesis.representation.service;

import com.home.genesis.Constants;
import com.home.genesis.logic.CellType;
import com.home.genesis.logic.entity.Cell;
import com.home.genesis.representation.Styles;
import com.home.genesis.representation.entity.GenomeTile;
import com.home.genesis.representation.entity.WorldTile;

import java.util.List;
import java.util.ListIterator;

public class WorldDataService {

    public WorldTile[][] getBotTiles(final Cell[][] initialCells) {
        final WorldTile[][] worldTileArray = new WorldTile[Constants.CELL_NUMBER_X][Constants.CELL_NUMBER_Y];
        initializeTilesAsEmpty(worldTileArray);
        for (int x = 0; x < Constants.CELL_NUMBER_X; x++) {
            for (int y = 0; y < Constants.CELL_NUMBER_Y; y++) {
                final Cell cell = initialCells[x][y];
                final CellType cellType = cell.getCellType();
                final WorldTile worldTile = new WorldTile(cell.getPositionX(), cell.getPositionY());
                final String styleClassName = Styles.valueOf(cellType.name()).getStyleName();
                worldTile.getTileBackground().getStyleClass().clear();
                worldTile.getTileBackground().getStyleClass().add(styleClassName);
                if (cellType.equals(CellType.BOT)) {
                    worldTile.getText().setText(String.valueOf(Constants.BOT_INITIAL_HEALTH));
                }
                worldTileArray[cell.getPositionX()][cell.getPositionY()] = worldTile;
            }
        }
        return worldTileArray;
    }

    public GenomeTile[][] getGenomeTiles(final List<Integer> genomeData) {
        final GenomeTile[][] genomeTilesArray = new GenomeTile[Constants.BOT_DNA_DIMENSION][Constants.BOT_DNA_DIMENSION];
        if (genomeData == null || genomeData.size() < Constants.BOT_DNA_COMMANDS) {
            return genomeTilesArray;
        }
        final ListIterator<Integer> integerListIterator = genomeData.listIterator();
        for (int x = 0; x < Constants.BOT_DNA_DIMENSION; x++) {
            for (int y = 0; y < Constants.BOT_DNA_DIMENSION; y++) {
                GenomeTile genomeTile = new GenomeTile(x, y);
                Integer gen = integerListIterator.next();
                genomeTile.getText().setText(String.valueOf(gen));
                genomeTilesArray[x][y] = genomeTile;
            }
        }
        return genomeTilesArray;
    }

    private void initializeTilesAsEmpty(final WorldTile[][] worldTileArray) {
        for (int x = 0; x < Constants.CELL_NUMBER_X; x++) {
            for (int y = 0; y < Constants.CELL_NUMBER_Y; y++) {
                final WorldTile worldTile = new WorldTile(x, y);
                final String styleClassName = Styles.EMPTY.getStyleName();
                worldTile.getTileBackground().getStyleClass().add(styleClassName);
                worldTileArray[x][y] = worldTile;
            }
        }
    }
}
