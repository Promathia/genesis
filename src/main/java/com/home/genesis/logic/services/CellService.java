package com.home.genesis.logic.services;

import com.home.genesis.logic.CellType;
import com.home.genesis.Constants;
import com.home.genesis.logic.entity.*;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class CellService {

    public Set<Cell> getInitialCells() {
        final int totalCellNumber = Constants.CELL_NUMBER_X * Constants.CELL_NUMBER_Y;
        final Set<Cell> cells = new HashSet<>(totalCellNumber);
        createFrameOfObstacles(cells);
        populateCellsCollection(cells, CellType.OBSTACLE, Constants.OBSTACLE_NUMBER_ON_MAP * Constants.OBSTACLE_SIZE);
        populateCellsCollection(cells, CellType.FOOD, Constants.FOOD_NUMBER);
        populateCellsCollection(cells, CellType.POISON, Constants.POISON_NUMBER);
        populateCellsCollection(cells, CellType.BOT, Constants.BOT_MAX_NUMBER);
        return cells;
    }

    private void createFrameOfObstacles(Set<Cell> cells) {
        for (int x = 0; x < Constants.CELL_NUMBER_X; x++) {
            cells.add(getCell(x, 0, CellType.OBSTACLE));
            cells.add(getCell(x, Constants.CELL_NUMBER_Y - 1, CellType.OBSTACLE)); //-1 is used as this is max not counting 0
        }
        for (int y = 1; y < Constants.CELL_NUMBER_Y - 1; y++) { //we exclude first and last as they were already created for x
            cells.add(getCell(0, y, CellType.OBSTACLE));
            cells.add(getCell(Constants.CELL_NUMBER_X - 1, y, CellType.OBSTACLE)); //-1 is used as this is max not counting 0
        }
    }

    private void populateCellsCollection(final Set<Cell> cells, CellType cellType, int number) {
        final Random random = new Random();
        for (int i = 0; i < number; i++) {
            boolean isAdded;
            do {
                int initialX = random.nextInt(Constants.CELL_NUMBER_X);
                int initialY = random.nextInt(Constants.CELL_NUMBER_Y);
                Cell cell = getCell(initialX, initialY, cellType);
                isAdded = !cells.add(cell);
            } while (isAdded);
        }
    }

    private Cell getCell(int initialX, int initialY, CellType cellType) {
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

}
