package com.home.genesis.logic.entity;

import java.util.HashSet;
import java.util.Set;

public class World {

    private int sizeX;
    private int sizeY;
    private Set<Obstacle> obstacleList = new HashSet<>();

    public World() {
    }

    public World(int sizeX, int sizeY) {
        this.sizeX = sizeX;
        this.sizeY = sizeY;
    }

    public World(int sizeX, int sizeY, Set<Obstacle> obstacleList) {
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.obstacleList = obstacleList;
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

    public Set<Obstacle> getObstacleList() {
        return obstacleList;
    }

    public void setObstacleList(Set<Obstacle> obstacleList) {
        this.obstacleList = obstacleList;
    }

    @Override
    public String toString() {
        return "World{" +
                "sizeX=" + sizeX +
                ", sizeY=" + sizeY +
                ", obstacleList=" + obstacleList +
                '}';
    }
}
