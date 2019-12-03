package com.home.genesis.representation;

public enum Styles {

    ROOT_PANE("root-pane"),
    WORLD_PANE("world-pane"),
    BUTTON_PANE("button-pane"),
    BUTTON_LEFT("button-left"),
    BUTTON_RIGHT("button-right"),

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
