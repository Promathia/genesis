package com.home.genesis.representation.entity;

import static com.home.genesis.general.Constants.TILE_SIZE;

import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

public class Tile extends StackPane {

    private int x;
    private int y;

    private Rectangle tileBackground = new Rectangle(TILE_SIZE, TILE_SIZE);
    private Text text = new Text();

    public Tile(final int x, final int y) {
        this.x = x;
        this.y = y;
        getChildren().addAll(tileBackground, text);
        setTranslateX(x * TILE_SIZE);
        setTranslateY(y * TILE_SIZE);
    }

    public Rectangle getTileBackground() {
        return tileBackground;
    }

    public Text getText() {
        return text;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
