package com.home.genesis.representation.service;

import com.home.genesis.logic.CellType;
import com.home.genesis.Constants;
import com.home.genesis.logic.entity.Cell;
import com.home.genesis.representation.Styles;
import com.home.genesis.representation.entity.Tile;

import java.util.Set;

public class WorldControllerService {

    public Tile[][] getTiles(final Set<Cell> initialCells) {
        final Tile[][] tileArray = new Tile[Constants.CELL_NUMBER_X][Constants.CELL_NUMBER_Y];
        initializeTilesAsEmpty(tileArray, CellType.EMPTY);
        for (Cell cell : initialCells) {
            CellType cellType = cell.getCellType();
            Tile tile = new Tile(cell.getPositionX(), cell.getPositionY());
            String styleClassName = Styles.valueOf(cellType.name()).getStyleName();
            tile.getTileBackground().getStyleClass().clear();
            tile.getTileBackground().getStyleClass().add(styleClassName);
            if (cellType.equals(CellType.BOT)) {
                tile.getText().setText(String.valueOf(Constants.BOT_INITIAL_HEALTH));
            }
            tileArray[cell.getPositionX()][cell.getPositionY()] = tile;
        }
        return tileArray;
    }

    private void initializeTilesAsEmpty(final Tile[][] tileArray, final CellType cellType) {
        for (int x = 0; x < Constants.CELL_NUMBER_X; x++) {
            for (int y = 0; y < Constants.CELL_NUMBER_Y; y++) {
                Tile tile = new Tile(x, y);
                String styleClassName = Styles.valueOf(cellType.name()).getStyleName();
                tile.getTileBackground().getStyleClass().add(styleClassName);
                tileArray[x][y] = tile;
            }
        }
    }
}
