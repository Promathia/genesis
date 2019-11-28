package com.home.genesis.logic.context;

import com.home.genesis.logic.CellType;
import com.home.genesis.Constants;
import com.home.genesis.logic.entity.Cell;
import com.home.genesis.logic.entity.Food;
import com.home.genesis.logic.entity.Poison;
import com.home.genesis.logic.entity.SingleBot;
import com.home.genesis.logic.services.CellService;

import java.util.Set;
import java.util.stream.Collectors;

public class SimulatorContext {

    private static SimulatorContext instance;
    private CellService cellService;
    private Set<Cell> initialCells;
    private Set<SingleBot> bots;
    private Set<Food> food;
    private Set<Poison> poison;
    private Cell[][] cellsArray;

    private SimulatorContext() {
        cellService = new CellService();
    }

    public static synchronized SimulatorContext getInstance() {
        if (instance == null) {
            instance = new SimulatorContext();
        }
        return instance;
    }

    public Set<Cell> getInitialCells() {
        if (initialCells == null || initialCells.isEmpty()) {
            initialCells = cellService.getInitialCells();
        }
        return initialCells;
    }

    public Set<SingleBot> getBots() {
        if (this.bots == null || this.bots.isEmpty()) {
            Set<Cell> cells = this.getInitialCells();
            this.bots = cells.stream()
                    .filter(cell -> cell.getCellType().equals(CellType.BOT))
                    .map(cell -> (SingleBot) cell)
                    .collect(Collectors.toSet());
        }
        return this.bots;
    }

    public Set<Food> getFood() {
        if (this.food == null || this.food.isEmpty()) {
            Set<Cell> cells = this.getInitialCells();
            this.food = cells.stream()
                    .filter(cell -> cell.getCellType().equals(CellType.FOOD))
                    .map(cell -> (Food) cell)
                    .collect(Collectors.toSet());
        }
        return this.food;
    }

    public Set<Poison> getPoison() {
        if (this.poison == null || this.poison.isEmpty()) {
            Set<Cell> cells = this.getInitialCells();
            this.poison = cells.stream()
                    .filter(cell -> cell.getCellType().equals(CellType.POISON))
                    .map(cell -> (Poison) cell)
                    .collect(Collectors.toSet());
        }
        return this.poison;
    }

    public Cell[][] getCellsArray() {
        if (this.cellsArray == null || this.cellsArray.length == 0) {
            this.cellsArray = new Cell[Constants.CELL_NUMBER_X][Constants.CELL_NUMBER_Y];
            Set<Cell> cells = this.getInitialCells();
            cells.forEach(cell -> {
                cellsArray[cell.getPositionX()][cell.getPositionY()] = cell;
            });
        }
        return this.cellsArray;
    }
}
