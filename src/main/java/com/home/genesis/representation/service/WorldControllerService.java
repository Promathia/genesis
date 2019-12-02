package com.home.genesis.representation.service;

import com.home.genesis.logic.CellType;
import com.home.genesis.Constants;
import com.home.genesis.logic.entity.Cell;
import com.home.genesis.representation.Styles;
import com.home.genesis.representation.entity.Tile;
import javafx.scene.layout.Pane;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class WorldControllerService {

    public Pane initializeView(final Tile[][] tilesArray) {
        final Pane rootPane = new Pane();
        rootPane.setPrefSize(Constants.SIZE_X, Constants.SIZE_Y);
        rootPane.getStyleClass().add(Styles.ROOT_PANE.getStyleName());
        rootPane.getChildren().addAll(Arrays.stream(tilesArray)
                .flatMap(Arrays::stream)
                .collect(Collectors.toList()));
        return rootPane;
    }

    public Tile[][] getTiles(final List<Cell> initialCells) {
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
