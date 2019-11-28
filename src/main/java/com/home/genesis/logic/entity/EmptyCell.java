package com.home.genesis.logic.entity;

import com.home.genesis.logic.CellType;

public class EmptyCell extends Cell {

    public EmptyCell(int positionX, int positionY) {
        super(positionX, positionY, CellType.EMPTY);
    }

}
