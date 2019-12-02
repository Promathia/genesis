package com.home.genesis.logic.services;

import com.home.genesis.logic.CellType;
import com.home.genesis.logic.context.SimulatorContext;
import com.home.genesis.logic.entity.Cell;
import com.home.genesis.logic.entity.Food;

import java.util.List;

public class FoodService {

    private SimulatorContext simulatorContext;
    private CellService cellService;

    public FoodService() {
        this.simulatorContext = SimulatorContext.getInstance();
        this.cellService = new CellService();
    }

    public void removeFood(Cell foodCell) {
        simulatorContext.getFood().remove(foodCell);
    }

    public Cell generateFood() {
        Food cell = (Food) cellService.generateCell(CellType.FOOD);
        Cell[][] cellsArray = simulatorContext.getCellsArray();
        cellsArray[cell.getPositionX()][cell.getPositionY()] = cell;
        List<Food> foods = simulatorContext.getFood();
        foods.add(cell);
        return cell;
    }
}
