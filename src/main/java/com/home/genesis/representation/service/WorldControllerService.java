package com.home.genesis.representation.service;

import com.home.genesis.logic.CellType;
import com.home.genesis.Constants;
import com.home.genesis.logic.entity.Cell;
import com.home.genesis.representation.StyleConstants;
import com.home.genesis.representation.Styles;
import com.home.genesis.representation.entity.WorldTile;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class WorldControllerService {

    private int currentActionPauseValue;

    public WorldControllerService(int currentActionPauseValue) {
        this.currentActionPauseValue = currentActionPauseValue;
    }

    public Pane initializeView(final WorldTile[][] tilesArray) {
        final Pane rootPane = new Pane();
        rootPane.setPrefSize(Constants.SIZE_X, Constants.SIZE_Y);
        rootPane.getStyleClass().add(Styles.ROOT_PANE.getStyleName());
        rootPane.getChildren().add(initializeWorldPane(tilesArray));
        rootPane.getChildren().add(initializeSpeedButtonPane());
        return rootPane;
    }

    public WorldTile[][] getTiles(final List<Cell> initialCells) {
        final WorldTile[][] worldTileArray = new WorldTile[Constants.CELL_NUMBER_X][Constants.CELL_NUMBER_Y];
        initializeTilesAsEmpty(worldTileArray, CellType.EMPTY);
        for (Cell cell : initialCells) {
            CellType cellType = cell.getCellType();
            WorldTile worldTile = new WorldTile(cell.getPositionX(), cell.getPositionY());
            String styleClassName = Styles.valueOf(cellType.name()).getStyleName();
            worldTile.getTileBackground().getStyleClass().clear();
            worldTile.getTileBackground().getStyleClass().add(styleClassName);
            if (cellType.equals(CellType.BOT)) {
                worldTile.getText().setText(String.valueOf(Constants.BOT_INITIAL_HEALTH));
            }
            worldTileArray[cell.getPositionX()][cell.getPositionY()] = worldTile;
        }
        return worldTileArray;
    }

    public int getCurrentActionPauseValue() {
        return currentActionPauseValue;
    }

    private Pane initializeWorldPane(final WorldTile[][] tilesArray) {
        final Pane worldPane = new Pane();
        worldPane.getStyleClass().add(Styles.WORLD_PANE.getStyleName());
        worldPane.getChildren().addAll(Arrays.stream(tilesArray)
                .flatMap(Arrays::stream)
                .collect(Collectors.toList()));
        worldPane.setLayoutX(StyleConstants.PADDING_MARGIN);
        worldPane.setLayoutY(StyleConstants.PADDING_MARGIN);
        return worldPane;
    }

    private Pane initializeSpeedButtonPane() {
        final Pane buttonPane = new Pane();
        buttonPane.getStyleClass().add(Styles.BUTTON_PANE.getStyleName());
        buttonPane.setLayoutX(StyleConstants.PADDING_MARGIN);
        buttonPane.setLayoutY(StyleConstants.PADDING_MARGIN * 2 + (Constants.TILE_SIZE * Constants.CELL_NUMBER_Y));
        Button buttonLeft = new Button();
        buttonLeft.getStyleClass().add(Styles.BUTTON_LEFT.getStyleName());
        Button buttonRight = new Button();
        Text text = new Text();
        text.setText(String.valueOf(currentActionPauseValue));
        text.getStyleClass().add(Styles.BUTTON_LEFT.getStyleName());
        buttonRight.getStyleClass().add(Styles.BUTTON_RIGHT.getStyleName());
        buttonRight.setLayoutX(25);
        buttonRight.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                currentActionPauseValue = currentActionPauseValue + 20;
                text.setText(String.valueOf(currentActionPauseValue));
            }
        });
        buttonLeft.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                if (currentActionPauseValue >= 20) {
                    currentActionPauseValue = currentActionPauseValue - 20;
                    text.setText(String.valueOf(currentActionPauseValue));
                }
            }
        });
        buttonPane.getChildren().addAll(buttonLeft, text, buttonRight);
        return buttonPane;
    }

    private void initializeTilesAsEmpty(final WorldTile[][] worldTileArray, final CellType cellType) {
        for (int x = 0; x < Constants.CELL_NUMBER_X; x++) {
            for (int y = 0; y < Constants.CELL_NUMBER_Y; y++) {
                WorldTile worldTile = new WorldTile(x, y);
                String styleClassName = Styles.valueOf(cellType.name()).getStyleName();
                worldTile.getTileBackground().getStyleClass().add(styleClassName);
                worldTileArray[x][y] = worldTile;
            }
        }
    }
}
