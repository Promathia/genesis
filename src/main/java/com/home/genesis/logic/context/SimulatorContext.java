package com.home.genesis.logic.context;

import com.home.genesis.logic.CellType;
import com.home.genesis.Constants;
import com.home.genesis.logic.entity.Cell;
import com.home.genesis.logic.entity.Food;
import com.home.genesis.logic.entity.Poison;
import com.home.genesis.logic.entity.SingleBot;
import com.home.genesis.logic.services.CellService;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class SimulatorContext {

    private static SimulatorContext instance;
    private final CellService cellService;
    private List<SingleBot> bots;
    private final AtomicInteger foodCounter;
    private final AtomicInteger poisonCounter;
    private final Cell[][] cellsArray = new Cell[Constants.CELL_NUMBER_X][Constants.CELL_NUMBER_Y];
    private final AtomicInteger generationCounter;

    private SimulatorContext() {
        this.generationCounter = new AtomicInteger(0);
        this.foodCounter = new AtomicInteger(0);
        this.poisonCounter = new AtomicInteger(0);
        this.cellService = new CellService();
        this.initializeCellsArray();
        this.initializeFoodCounter();
        this.initializePoisonCounter();
    }

    public static synchronized SimulatorContext getInstance() {
        if (instance == null) {
            instance = new SimulatorContext();
        }
        return instance;
    }

    //TODO generalize, used in two places
    public List<Integer> getInitialGenome() {
        Optional<SingleBot> first = this.getBots().stream().max(Comparator.comparingInt(SingleBot::getActionsCounter));
        return first.isPresent() ? first.get().getDnaCommands() : new ArrayList<>();
    }

    public List<SingleBot> getBots() {
        if (this.bots == null) {
            this.bots = Arrays.stream(cellsArray)
                    .flatMap(Arrays::stream)
                    .filter(cell -> cell.getCellType().equals(CellType.BOT))
                    .map(cell -> (SingleBot) cell)
                    .collect(Collectors.toList());
        }
        return this.bots;
    }

    public Cell[][] getCellsArray() {
        return cellsArray;
    }

    public int incrementAndGetGenerationCounter() {
        return generationCounter.incrementAndGet();
    }

    public int getPoisonNumber() {
        return poisonCounter.get();
    }

    public int getFoodNumber() {
        return foodCounter.get();
    }

    public int incrementAndGetPoisonCounter() {
        return poisonCounter.incrementAndGet();
    }

    public int incrementAndGetFoodCounter() {
        return foodCounter.incrementAndGet();
    }

    public int decrementPoisonCounter() {
        return poisonCounter.decrementAndGet();
    }

    public int decrementFoodCounter() {
        return foodCounter.decrementAndGet();
    }

    private void initializeCellsArray() {
        final Set<Cell> initialCells = cellService.getInitialCells();
        initialCells.forEach(cell -> {
            cellsArray[cell.getPositionX()][cell.getPositionY()] = cell;
        });
    }

    private void initializePoisonCounter() {
        long count = Arrays.stream(cellsArray)
                .flatMap(Arrays::stream)
                .filter(cell -> cell.getCellType().equals(CellType.POISON))
                .count();
        poisonCounter.set((int) count);
    }

    private void initializeFoodCounter() {
        long count = Arrays.stream(cellsArray)
                .flatMap(Arrays::stream)
                .filter(cell -> cell.getCellType().equals(CellType.FOOD))
                .count();
        foodCounter.set((int) count);
    }

}
