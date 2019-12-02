package com.home.genesis.logic.services;

import com.home.genesis.logic.CellType;
import com.home.genesis.logic.context.SimulatorContext;
import com.home.genesis.logic.entity.Cell;
import com.home.genesis.logic.entity.Poison;

import java.util.List;

public class PoisonService {

    private SimulatorContext simulatorContext;
    private CellService cellService;

    public PoisonService() {
        this.simulatorContext = SimulatorContext.getInstance();
        this.cellService = new CellService();
    }

    public void removePoison(Cell poisonCell) {
        simulatorContext.getPoison().remove(poisonCell);
    }

    public Cell generatePoison() {
        Poison cell = (Poison) cellService.generateCell(CellType.POISON);
        Cell[][] cellsArray = simulatorContext.getCellsArray();
        cellsArray[cell.getPositionX()][cell.getPositionY()] = cell;
        List<Poison> poisons = simulatorContext.getPoison();
        poisons.add(cell);
        return cell;
    }

    public void convertPoisonToFood(Cell poisonCell) {
        Cell[][] cellsArray = simulatorContext.getCellsArray();
        int positionX = poisonCell.getPositionX();
        int positionY = poisonCell.getPositionY();
        Cell foodCell = cellService.createCell(positionX, positionY, CellType.FOOD);
        cellsArray[positionX][positionY] = foodCell;
    }
}
