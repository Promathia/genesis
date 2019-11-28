package com.home.genesis.representation.controllers;

import com.home.genesis.Constants;
import com.home.genesis.logic.entity.Cell;
import com.home.genesis.logic.entity.SingleBot;
import com.home.genesis.representation.Styles;
import com.home.genesis.representation.entity.Tile;
import com.home.genesis.representation.service.WorldControllerService;
import javafx.scene.layout.Pane;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import static com.home.genesis.Constants.TILE_SIZE;

public class WorldViewController {

    private final WorldControllerService worldControllerService = new WorldControllerService();

    private Tile[][] tilesArray;
    //private List<Tile> tiles;

    public Pane initializeView(final Set<Cell> initialCells) {
        final Pane rootPane = new Pane();
        rootPane.setPrefSize(Constants.SIZE_X, Constants.SIZE_Y);
        rootPane.getStyleClass().add(Styles.ROOT_PANE.getStyleName());
        tilesArray = worldControllerService.getTiles(initialCells);
        rootPane.getChildren().addAll(Arrays.stream(tilesArray)
                .flatMap(Arrays::stream)
                .collect(Collectors.toList()));
        return rootPane;
    }

    public void moveBot(final SingleBot singleBot) {
        Tile tile1 = tilesArray[singleBot.getPositionX()][singleBot.getPositionY()];
        tile1.getTileBackground().getStyleClass().clear();
        tile1.getTileBackground().getStyleClass().add("active-tile");
        tile1.setTranslateX(tile1.getTranslateX() - TILE_SIZE);
    }


}
