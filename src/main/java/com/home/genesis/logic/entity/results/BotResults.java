package com.home.genesis.logic.entity.results;

import com.home.genesis.logic.actions.ActionsResult;

public class BotResults {
    private ActionsResult actionsResult;
    private int x;
    private int y;
    private String payload;

    public BotResults(ActionsResult actionsResult, int x, int y, String payload) {
        this.actionsResult = actionsResult;
        this.x = x;
        this.y = y;
        this.payload = payload;
    }

    public ActionsResult getActionsResult() {
        return actionsResult;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public String getPayload() {
        return payload;
    }
}
