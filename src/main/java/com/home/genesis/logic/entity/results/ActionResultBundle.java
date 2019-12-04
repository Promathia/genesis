package com.home.genesis.logic.entity.results;

import com.home.genesis.logic.actions.ActionsResult;

import java.util.*;

public final class ActionResultBundle {

    private List<BotResults> botResults;
    private List<GenomeResults> genomeResults;
    private Integer generationCounter;
    private Integer botBestActionCounter;

    public ActionResultBundle() {
        this.botResults = new ArrayList<>();
        this.genomeResults = new ArrayList<>();
        this.generationCounter = null;
        this.botBestActionCounter = null;
    }

    public void addBotActionResult(ActionsResult actionsResult, int x, int y, String payload) {
        this.botResults.add(new BotResults(actionsResult, x, y, payload));
    }

    public void addGenomeResult(List<Integer> payload) {
        this.genomeResults.add(new GenomeResults(payload));
    }

    public List<BotResults> getBotResults() {
        return botResults;
    }

    public List<GenomeResults> getGenomeResults() {
        return genomeResults;
    }

    public Integer getGenerationCounter() {
        return generationCounter;
    }

    public void setGenerationCounter(Integer generationCounter) {
        this.generationCounter = generationCounter;
    }

    public Integer getBotBestActionCounter() {
        return botBestActionCounter;
    }

    public void setBotBestActionCounter(Integer botBestActionCounter) {
        this.botBestActionCounter = botBestActionCounter;
    }
}
