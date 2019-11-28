package com.home.genesis.logic.entity;

import com.home.genesis.general.CellType;

public class Food extends Cell {

    public Food(int positionX, int positionY) {
        super(positionX, positionY, CellType.FOOD);
    }
}
