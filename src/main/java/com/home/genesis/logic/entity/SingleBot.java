package com.home.genesis.logic.entity;

import com.home.genesis.logic.CellType;
import com.home.genesis.Constants;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SingleBot extends Cell {

    private List<Integer> dnaCommands = new ArrayList<>(Constants.BOT_DNA_COMMANDS);
    private int currentStep;
    private int health;
    private int direction;

    public SingleBot(List<Integer> dnaCommands, int positionX, int positionY) {
        super(positionX, positionY, CellType.BOT);
        this.dnaCommands.addAll(dnaCommands);
        this.direction = 0; //TODO
        this.health = Constants.BOT_INITIAL_HEALTH;
        this.currentStep = 0;
    }

    public SingleBot(int positionX, int positionY) {
        super(positionX, positionY, CellType.BOT);
        Random random = new Random(positionX * positionY);
        for (int i = 0; i < Constants.BOT_DNA_COMMANDS; i++) {
            int command = random.nextInt(Constants.BOT_DNA_COMMANDS);
            dnaCommands.add(command);
        }
        this.direction = 0; //TODO
        this.health = Constants.BOT_INITIAL_HEALTH;
        this.currentStep = 0;
    }

    public List<Integer> getDnaCommands() {
        return dnaCommands;
    }

    public void setDnaCommands(List<Integer> dnaCommands) {
        this.dnaCommands = dnaCommands;
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
}
