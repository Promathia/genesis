package com.home.genesis.logic.services;

import com.home.genesis.logic.CellType;
import com.home.genesis.logic.context.SimulatorContext;
import com.home.genesis.logic.entity.Cell;
import com.home.genesis.logic.entity.Poison;

public class PoisonService {

    private CellService cellService;

    public PoisonService() {
        this.cellService = new CellService();
    }

    public void removePoison(final Poison poisonCell) {
        SimulatorContext.getInstance().decrementPoisonCounter();
        int positionX = poisonCell.getPositionX();
        int positionY = poisonCell.getPositionY();
        SimulatorContext.getInstance().getCellsArray()[positionX][positionY] =
                cellService.createCell(positionX, positionY, CellType.EMPTY);
    }

    public Cell generatePoison() {
        Poison poisonCell = (Poison) cellService.generateCell(CellType.POISON);
        Cell[][] cellsArray = SimulatorContext.getInstance().getCellsArray();
        cellsArray[poisonCell.getPositionX()][poisonCell.getPositionY()] = poisonCell;
        SimulatorContext.getInstance().incrementAndGetPoisonCounter();
        return poisonCell;
    }

}
