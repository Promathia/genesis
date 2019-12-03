package com.home.genesis.general.process;

import com.home.genesis.Constants;
import com.home.genesis.general.services.LifeSimulatorService;
import com.home.genesis.logic.context.SimulatorContext;
import com.home.genesis.logic.entity.ActionResultBundle;
import com.home.genesis.logic.entity.Cell;
import com.home.genesis.logic.entity.SingleBot;
import com.home.genesis.logic.services.BotService;

import java.util.Collections;
import java.util.List;
import java.util.ListIterator;
import java.util.concurrent.BlockingQueue;

public class RunSimulation implements Runnable {

    private SimulatorContext simulatorContext;
    private LifeSimulatorService lifeSimulatorService;
    private BotService botService;
    private BlockingQueue<ActionResultBundle> resultsQueue;

    public RunSimulation(BlockingQueue<ActionResultBundle> resultsQueue, LifeSimulatorService lifeSimulatorService) {
        this.simulatorContext = SimulatorContext.getInstance();
        this.botService = new BotService();
        this.resultsQueue = resultsQueue;
        this.lifeSimulatorService = lifeSimulatorService;
    }

    @Override
    public void run() {
        while (true) {
            try {
                handleBotActions();
                Thread.sleep(lifeSimulatorService.getPauseTimeBetweenActions());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void handleBotActions() throws InterruptedException {
        final List<SingleBot> bots = simulatorContext.getBots();
        final Cell[][] cellsArray = simulatorContext.getCellsArray();
        Collections.shuffle(bots);
        final ListIterator<SingleBot> singleBotListIterator = bots.listIterator();
        while (singleBotListIterator.hasNext()) {
            final ActionResultBundle actionResultBundle = new ActionResultBundle();
            if (bots.size() <= Constants.BOT_MIN_NUMBER) {
                botService.handleNewBotsCreation(singleBotListIterator, actionResultBundle);
                resultsQueue.put(actionResultBundle);
                simulatorContext.getGenerationCounter().incrementAndGet();
                break;
            }
            final SingleBot singleBot = singleBotListIterator.next();
            botService.handleBotDecisionTaking(singleBot, cellsArray, actionResultBundle);
            botService.handleBotDeathOrConsumeCalorie(singleBot, singleBotListIterator, actionResultBundle);
            resultsQueue.put(actionResultBundle);
        }
    }
}
