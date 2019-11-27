package com.home.genesis.logic.entity;

import java.util.Objects;

public class Obstacle {

    private int positionX;
    private int positionY;

    public Obstacle() {
    }

    public Obstacle(int positionX, int positionY) {
        this.positionX = positionX;
        this.positionY = positionY;
    }

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Obstacle obstacle = (Obstacle) o;
        return positionX == obstacle.positionX &&
                positionY == obstacle.positionY;
    }

    @Override
    public int hashCode() {
        return Objects.hash(positionX, positionY);
    }

    @Override
    public String toString() {
        return "Obstacle{" +
                "positionX=" + positionX +
                ", positionY=" + positionY +
                '}';
    }
}
