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

    public void removeFood(Food foodCell) {
        simulatorContext.getFood().remove(foodCell);
    }

    public void addFood(Food foodCell) {
        List<Food> foods = simulatorContext.getFood();
        foods.add(foodCell);
    }

    public Cell generateFood() {
        Food foodCell = (Food) cellService.generateCell(CellType.FOOD);
        //Cell[][] cellsArray = simulatorContext.getCellsArray();
        //cellsArray[foodCell.getPositionX()][foodCell.getPositionY()] = foodCell;
        this.addFood(foodCell);
        return foodCell;
    }
}
