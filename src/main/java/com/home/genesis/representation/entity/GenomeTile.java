package com.home.genesis.representation.entity;

import com.home.genesis.Constants;
import com.home.genesis.representation.Styles;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class GenomeTile extends StackPane {

    private int x;
    private int y;

    private Rectangle tileBackground = new Rectangle(Constants.STATS_TILE_SIZE, Constants.STATS_TILE_SIZE);
    private Text text = new Text();

    public GenomeTile(final int x, final int y) {
        this.x = x;
        this.y = y;
        this.tileBackground.getStyleClass().add(Styles.GENOME_TILE.getStyleName());
        this.text.getStyleClass().add(Styles.GENOME_TEXT.getStyleName());
        getChildren().addAll(tileBackground, text);
        setTranslateX(x * Constants.STATS_TILE_SIZE);
        setTranslateY(y * Constants.STATS_TILE_SIZE);
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
