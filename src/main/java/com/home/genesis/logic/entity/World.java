package com.home.genesis.logic.entity;

import java.util.HashSet;
import java.util.Set;

public class World {

    private int sizeX;
    private int sizeY;
    private Set<Cell> obstacles = new HashSet<>();

    public World() {
    }

    public World(int sizeX, int sizeY) {
        this.sizeX = sizeX;
        this.sizeY = sizeY;
    }

    public World(int sizeX, int sizeY, Set<Cell> obstacles) {
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.obstacles = obstacles;
    }

    public int getSizeX() {
        return sizeX;
    }

    public void setSizeX(int sizeX) {
        this.sizeX = sizeX;
    }

    public int getSizeY() {
        return sizeY;
    }

    public void setSizeY(int sizeY) {
        this.sizeY = sizeY;
    }

    public Set<Cell> getObstacles() {
        return obstacles;
    }

    public void setObstacles(Set<Cell> obstacles) {
        this.obstacles = obstacles;
    }

    @Override
    public String toString() {
        return "World{" +
                "sizeX=" + sizeX +
                ", sizeY=" + sizeY +
                ", obstacleList=" + obstacles +
                '}';
    }
}
