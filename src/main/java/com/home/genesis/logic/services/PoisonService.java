package com.home.genesis.logic.services;

import com.home.genesis.logic.CellType;
import com.home.genesis.logic.context.SimulatorContext;
import com.home.genesis.logic.entity.Cell;
import com.home.genesis.logic.entity.Food;
import com.home.genesis.logic.entity.Poison;

import java.util.List;

public class PoisonService {

    private SimulatorContext simulatorContext;
    private CellService cellService;

    public PoisonService() {
        this.simulatorContext = SimulatorContext.getInstance();
        this.cellService = new CellService();
    }

    public void removePoison(Poison poisonCell) {
        simulatorContext.getPoison().remove(poisonCell);
    }

    public void addPoison(Poison poisonCell) {
        List<Poison> poisons = simulatorContext.getPoison();
        poisons.add(poisonCell);
    }

    public Cell generatePoison() {
        Poison poisonCell = (Poison) cellService.generateCell(CellType.POISON);
        //Cell[][] cellsArray = simulatorContext.getCellsArray();
        //cellsArray[poisonCell.getPositionX()][poisonCell.getPositionY()] = poisonCell;
        this.addPoison(poisonCell);
        return poisonCell;
    }

    public Food convertPoisonToFood(int resultX, int resultY) {
        Cell[][] cellsArray = simulatorContext.getCellsArray();
        Food foodCell = (Food) cellService.createCell(resultX, resultY, CellType.FOOD);
        cellsArray[resultX][resultY] = foodCell;
        return foodCell;
    }
}
