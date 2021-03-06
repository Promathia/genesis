package com.home.genesis.logic.entity;

import com.home.genesis.logic.CellType;
import com.home.genesis.Constants;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class SingleBot extends Cell {

    private final List<Integer> dnaCommands = new ArrayList<>(Constants.BOT_DNA_COMMANDS);
    private int currentStep;
    private int health;
    private int direction;
    private int actionsCounter;
    private int nonTerminalCommandCounter;

    public SingleBot(final int positionX, final int positionY, final SingleBot singleBot) {
        super(positionX, positionY, CellType.BOT);
        this.dnaCommands.addAll(singleBot.getDnaCommands());
        this.direction = ThreadLocalRandom.current().nextInt(8);
        this.health = Constants.BOT_INITIAL_HEALTH;
        this.currentStep = singleBot.getCurrentStep();
        this.nonTerminalCommandCounter = Constants.BOT_NON_TERMINAL_COMMAND_COUNTER_MAX;
        this.actionsCounter = 0;
    }

    public SingleBot(int positionX, int positionY) {
        super(positionX, positionY, CellType.BOT);
        for (int i = 0; i < Constants.BOT_DNA_COMMANDS; i++) {
            int command = ThreadLocalRandom.current().nextInt(Constants.BOT_DNA_COMMANDS);
            dnaCommands.add(command);
        }
        this.direction = 0;
        this.health = Constants.BOT_INITIAL_HEALTH;
        this.currentStep = 0;
        this.nonTerminalCommandCounter = Constants.BOT_NON_TERMINAL_COMMAND_COUNTER_MAX;
        this.actionsCounter = 0;
    }

    public List<Integer> getDnaCommands() {
        return dnaCommands;
    }

    public int getCurrentStep() {
        return currentStep;
    }

    public void setCurrentStep(int currentStep) {
        this.currentStep = currentStep;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public int getActionsCounter() {
        return actionsCounter;
    }

    public void setActionsCounter(int actionsCounter) {
        this.actionsCounter = actionsCounter;
    }

    public int getNonTerminalCommandCounter() {
        return nonTerminalCommandCounter;
    }

    public void setNonTerminalCommandCounter(int nonTerminalCommandCounter) {
        this.nonTerminalCommandCounter = nonTerminalCommandCounter;
    }
}
