package com.home.genesis.logic.services;

import com.home.genesis.logic.CellType;
import com.home.genesis.Constants;
import com.home.genesis.logic.context.SimulatorContext;
import com.home.genesis.logic.entity.*;

import java.util.*;

public class CellService {

    public CellService() {
    }

    public Set<Cell> getInitialCells() {
        final int totalCellNumber = Constants.CELL_NUMBER_X * Constants.CELL_NUMBER_Y;
        final Set<Cell> cells = new HashSet<>(totalCellNumber);
        createFrameOfObstacles(cells);
        populateCellsCollection(cells, CellType.OBSTACLE, Constants.OBSTACLE_NUMBER_ON_MAP * Constants.OBSTACLE_SIZE);
        populateCellsCollection(cells, CellType.FOOD, Constants.FOOD_NUMBER);
        populateCellsCollection(cells, CellType.POISON, Constants.POISON_NUMBER);
        populateCellsCollection(cells, CellType.BOT, Constants.BOT_MAX_NUMBER);
        addEmptyCells(cells);
        return cells;
    }

    public Cell generateRandomCellByType(final CellType cellType) {
        final Random random = new Random();
        Cell[][] cellsArray = SimulatorContext.getInstance().getCellsArray();
        Cell newCell = null;
        int initialX;
        int initialY;
        boolean cellGenerated = false;
        do {
            initialX = random.nextInt(Constants.CELL_NUMBER_X);
            initialY = random.nextInt(Constants.CELL_NUMBER_Y);
            Cell cell = cellsArray[initialX][initialY];
            if (cell.getCellType() == CellType.EMPTY) {
                newCell = this.createCell(initialX, initialY, cellType);
                cellGenerated = true;
            }
        } while (!cellGenerated);
        return newCell;
    }

    public Cell getRandomEmptyCell() {
        final Random random = new Random(System.currentTimeMillis());
        Cell[][] cellsArray = SimulatorContext.getInstance().getCellsArray();
        Cell cell;
        boolean cellFound = false;
        do {
            int x = random.nextInt(Constants.CELL_NUMBER_X);
            int y = random.nextInt(Constants.CELL_NUMBER_Y);
            cell = cellsArray[x][y];
            if (cell.getCellType() == CellType.EMPTY) {
                cellFound = true;
            }
        } while (!cellFound);
        return cell;
    }

    public Cell createCell(final int initialX, final int initialY, final CellType cellType) {
        Cell cell;
        switch (cellType) {
            case OBSTACLE:
                cell = new Obstacle(initialX, initialY);
                break;
            case FOOD:
                cell = new Food(initialX, initialY);
                break;
            case BOT:
                cell = new SingleBot(initialX, initialY);
                break;
            case POISON:
                cell = new Poison(initialX, initialY);
                break;
            default:
                cell = new EmptyCell(initialX, initialY);
        }
        return cell;
    }

    private void createFrameOfObstacles(final Set<Cell> cells) {
        for (int x = 0; x < Constants.CELL_NUMBER_X; x++) {
            cells.add(this.createCell(x, 0, CellType.OBSTACLE));
            cells.add(this.createCell(x, Constants.CELL_NUMBER_Y - 1, CellType.OBSTACLE)); //-1 is used as this is max not counting 0
        }
        for (int y = 1; y < Constants.CELL_NUMBER_Y - 1; y++) { //we exclude first and last as they were already created for x
            cells.add(this.createCell(0, y, CellType.OBSTACLE));
            cells.add(this.createCell(Constants.CELL_NUMBER_X - 1, y, CellType.OBSTACLE)); //-1 is used as this is max not counting 0
        }
    }

    private void addEmptyCells(final Set<Cell> cells) {
        for (int x = 1; x < Constants.CELL_NUMBER_X - 1; x++) {
            for (int y = 1; y < Constants.CELL_NUMBER_Y - 1; y++) {
                cells.add(this.createCell(x, y, CellType.EMPTY));
            }
        }
    }

    private void populateCellsCollection(final Set<Cell> cells, final CellType cellType, final int number) {
        final Random random = new Random();
        for (int i = 0; i < number; i++) {
            boolean isAdded;
            do {
                int initialX = random.nextInt(Constants.CELL_NUMBER_X);
                int initialY = random.nextInt(Constants.CELL_NUMBER_Y);
                Cell cell = this.createCell(initialX, initialY, cellType);
                isAdded = !cells.add(cell);
            } while (isAdded);
        }
    }
}
