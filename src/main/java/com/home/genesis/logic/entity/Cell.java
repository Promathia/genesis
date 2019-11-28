package com.home.genesis.logic.entity;

import com.home.genesis.general.CellType;

import java.util.Objects;

public abstract class Cell {

    private int positionX;
    private int positionY;
    private CellType cellType;

    public Cell(int positionX, int positionY, CellType cellType) {
        this.positionX = positionX;
        this.positionY = positionY;
        this.cellType = cellType;
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

    public CellType getCellType() {
        return cellType;
    }

    public void setCellType(CellType cellType) {
        this.cellType = cellType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        Cell cell = (Cell) o;
        return positionX == cell.positionX &&
                positionY == cell.positionY;
    }

    @Override
    public int hashCode() {
        return Objects.hash(positionX, positionY);
    }
}
