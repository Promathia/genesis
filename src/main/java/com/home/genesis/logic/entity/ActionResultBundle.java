package com.home.genesis.logic.entity;

import com.home.genesis.logic.actions.ActionsResult;

import java.util.*;

public final class ActionResultBundle {

    private List<Result> results;

    public ActionResultBundle() {
        this.results = new ArrayList<>();
    }

    public void addActionResult(ActionsResult actionsResult, int x, int y, String payload) {
        this.results.add(new Result(actionsResult, x, y, payload));
    }

    public List<Result> getResults() {
        return results;
    }

    public class Result {
        private ActionsResult actionsResult;
        private int x;
        private int y;
        private String payload;

        public Result(ActionsResult actionsResult, int x, int y, String payload) {
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
}
