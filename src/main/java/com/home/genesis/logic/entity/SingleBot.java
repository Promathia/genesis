package com.home.genesis.logic.entity;

import java.util.ArrayList;
import java.util.List;

public class SingleBot {

    private int positionX;
    private int positionY;
    private List<Integer> dnaCommands = new ArrayList<>();
    private int currentStep;
    private int health;

    public int getPositionX() {
        return positionX;
    }

    public void setPositionX(int positionX) {
        this.positionX = positionX;
    }

    public int getPositionY() {
        return positionY;
    }

    public void setPositionY(int positionY) {
        this.positionY = positionY;
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
}
