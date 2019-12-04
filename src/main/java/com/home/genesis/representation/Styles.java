package com.home.genesis.representation;

public enum Styles {

    ROOT_PANE("root-pane"),
    WORLD_PANE("world-pane"),
    BUTTON_PANE("button-pane"),
    BUTTON_LEFT("button-left"),
    BUTTON_RIGHT("button-right"),
    SPEED_COUNTER_TEXT("speed-counter-text"),
    PLAY_BUTTON("button-play"),
    PAUSE_BUTTON("button-pause"),

    GENOME_TILE("genome-tile"),
    GENOME_TEXT("genome-text"),
    GENOME_TABLE_LABEL("genome-table-label"),
    GENERATION_COUNTER("generation-counter"),
    BOT_BEST_ACTION_COUNTER("bot-best-action-counter"),
    GENERATION_ACTION_CHART("generation-action-chart"),

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
