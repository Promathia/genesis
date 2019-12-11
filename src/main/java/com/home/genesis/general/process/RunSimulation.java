package com.home.genesis.general.process;

import com.home.genesis.Constants;
import com.home.genesis.general.services.LifeSimulatorService;
import com.home.genesis.general.services.StatisticsService;
import com.home.genesis.logic.context.SimulatorContext;
import com.home.genesis.logic.entity.results.ActionResultBundle;
import com.home.genesis.logic.entity.Cell;
import com.home.genesis.logic.entity.SingleBot;
import com.home.genesis.logic.services.BotService;

import java.util.Collections;
import java.util.List;
import java.util.ListIterator;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Semaphore;

public class RunSimulation implements Runnable {

    private final LifeSimulatorService lifeSimulatorService;
    private final BotService botService;
    private final StatisticsService statisticsService;
    private final BlockingQueue<ActionResultBundle> resultsQueue;
    private final Semaphore semaphore;

    public RunSimulation(final BlockingQueue<ActionResultBundle> resultsQueue,
                         final Semaphore semaphore,
                         final LifeSimulatorService lifeSimulatorService) {
        this.botService = new BotService();
        this.resultsQueue = resultsQueue;
        this.lifeSimulatorService = lifeSimulatorService;
        this.statisticsService = new StatisticsService();
        this.semaphore = semaphore;
    }

    @Override
    public void run() {
        while (true) {
            try {
                handleBotActions();
                Thread.sleep(lifeSimulatorService.getPauseTimeBetweenActions());
                semaphore.acquire();
                semaphore.release();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void handleBotActions() throws InterruptedException {
        final List<SingleBot> bots = SimulatorContext.getInstance().getBots();
        Collections.shuffle(bots);
        final ListIterator<SingleBot> botsListIterator = bots.listIterator();
        while (botsListIterator.hasNext()) {
            final ActionResultBundle actionResultBundle = new ActionResultBundle();
            if (bots.size() <= Constants.BOT_MIN_NUMBER) {
                int generationNumber = SimulatorContext.getInstance().incrementAndGetGenerationCounter();
                statisticsService.writeDataToFile(bots, generationNumber);
                botService.updateStatisticsResults(bots, generationNumber, actionResultBundle);
                botService.handleNewBotsCreation(botsListIterator, actionResultBundle);
                resultsQueue.put(actionResultBundle);
                break;
            }
            final SingleBot singleBot = botsListIterator.next();
            this.executeCommand(singleBot, actionResultBundle);
            botService.handleBotDeathOrAction(singleBot, botsListIterator, actionResultBundle);
            resultsQueue.put(actionResultBundle);
        }
    }

    private void executeCommand(final SingleBot singleBot, final ActionResultBundle actionResultBundle) {
        boolean isTerminalOperation = botService.handleBotDecisionTaking(singleBot, actionResultBundle);
        if (!isTerminalOperation) {
            int nonTerminalCommandCounter = singleBot.getNonTerminalCommandCounter();
            while(!isTerminalOperation && --nonTerminalCommandCounter > 0) {
                isTerminalOperation = botService.handleBotDecisionTaking(singleBot, actionResultBundle);
            }
        }
        singleBot.setNonTerminalCommandCounter(Constants.BOT_NON_TERMINAL_COMMAND_COUNTER_MAX);
    }
}
