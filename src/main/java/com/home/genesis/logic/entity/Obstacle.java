package com.home.genesis.logic.entity;

import com.home.genesis.general.CellType;

public class Obstacle extends Cell {

    public Obstacle(int positionX, int positionY) {
        super(positionX, positionY, CellType.OBSTACLE);
    }
}
