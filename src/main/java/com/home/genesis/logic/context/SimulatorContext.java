package com.home.genesis.logic.context;

import com.home.genesis.logic.CellType;
import com.home.genesis.Constants;
import com.home.genesis.logic.entity.Cell;
import com.home.genesis.logic.entity.Food;
import com.home.genesis.logic.entity.Poison;
import com.home.genesis.logic.entity.SingleBot;
import com.home.genesis.logic.services.CellService;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class SimulatorContext {

    private static SimulatorContext instance;
    private CellService cellService;
    private List<Cell> initialCells;
    private List<SingleBot> bots;
    private List<Food> food;
    private List<Poison> poison;
    private Cell[][] cellsArray;
    private AtomicInteger generationCounter;

    private SimulatorContext() {
        generationCounter = new AtomicInteger(0);
    }

    public static synchronized SimulatorContext getInstance() {
        if (instance == null) {
            instance = new SimulatorContext();
        }
        return instance;
    }

    public List<Cell> getInitialCells() {
        if (initialCells == null || initialCells.isEmpty()) {
            if (cellService == null) {
                cellService = new CellService(); //TODO !!!!!!!!!!!!
            }
            initialCells = cellService.getInitialCells();
        }
        return initialCells;
    }

    public List<SingleBot> getBots() {
        if (this.bots == null) {
            List<Cell> cells = this.getInitialCells();
            this.bots = cells.stream()
                    .filter(cell -> cell.getCellType().equals(CellType.BOT))
                    .map(cell -> (SingleBot) cell)
                    .collect(Collectors.toList());
        }
        return this.bots;
    }

    public List<Food> getFood() {
        if (this.food == null) {
            List<Cell> cells = this.getInitialCells();
            this.food = cells.stream()
                    .filter(cell -> cell.getCellType().equals(CellType.FOOD))
                    .map(cell -> (Food) cell)
                    .collect(Collectors.toList());
        }
        return this.food;
    }

    public List<Poison> getPoison() {
        if (this.poison == null) {
            List<Cell> cells = this.getInitialCells();
            this.poison = cells.stream()
                    .filter(cell -> cell.getCellType().equals(CellType.POISON))
                    .map(cell -> (Poison) cell)
                    .collect(Collectors.toList());
        }
        return this.poison;
    }

    public Cell[][] getCellsArray() {
        if (this.cellsArray == null) {
            this.cellsArray = new Cell[Constants.CELL_NUMBER_X][Constants.CELL_NUMBER_Y];
            List<Cell> cells = this.getInitialCells();
            cells.forEach(cell -> {
                cellsArray[cell.getPositionX()][cell.getPositionY()] = cell;
            });
        }
        return this.cellsArray;
    }

    public AtomicInteger getGenerationCounter() {
        return generationCounter;
    }
}
