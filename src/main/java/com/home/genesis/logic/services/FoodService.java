package com.home.genesis.logic.services;

import com.home.genesis.logic.CellType;
import com.home.genesis.logic.context.SimulatorContext;
import com.home.genesis.logic.entity.Cell;
import com.home.genesis.logic.entity.Food;

public class FoodService {

    private CellService cellService;

    public FoodService() {
        this.cellService = new CellService();
    }

    public void removeFood(final Food foodCell) {
        SimulatorContext.getInstance().decrementFoodCounter();
        int positionX = foodCell.getPositionX();
        int positionY = foodCell.getPositionY();
        SimulatorContext.getInstance().getCellsArray()[positionX][positionY] =
                cellService.createCell(positionX, positionY, CellType.EMPTY);
    }

    public void addFood(final Food foodCell) {
        SimulatorContext.getInstance().incrementAndGetFoodCounter();
        Cell[][] cellsArray = SimulatorContext.getInstance().getCellsArray();
        cellsArray[foodCell.getPositionX()][foodCell.getPositionY()] = foodCell;
    }

    public Cell generateFood() {
        Food foodCell = (Food) cellService.generateCell(CellType.FOOD);
        Cell[][] cellsArray = SimulatorContext.getInstance().getCellsArray();
        cellsArray[foodCell.getPositionX()][foodCell.getPositionY()] = foodCell;
        SimulatorContext.getInstance().incrementAndGetFoodCounter();
        return foodCell;
    }
}
