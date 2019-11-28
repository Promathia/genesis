package com.home.genesis.representation;

public enum Styles {

    ROOT_PANE("root-pane"),
    EMPTY("empty-tile"),
    POISON("poison-tile"),
    BOT("bot-tile"),
    OBSTACLE("obstacle-tile"),
    FOOD("food-tile");

    private String styleName;

    Styles(final String styleName) {
        this.styleName = styleName;
    }

    public String getStyleName() {
        return styleName;
    }
}
