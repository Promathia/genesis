package com.home.genesis.representation.entity;

import static com.home.genesis.Constants.TILE_SIZE;

import com.home.genesis.Constants;
import com.home.genesis.representation.Styles;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class WorldTile extends StackPane {

    private int x;
    private int y;

    private Rectangle tileBackground = new Rectangle(TILE_SIZE, TILE_SIZE);
    private Text text = new Text();

    public WorldTile(final int x, final int y) {
        this.x = x;
        this.y = y;
        text.setFont(Font.font ("Tahoma", Constants.BOT_TILE_TEXT_SIZE));
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
