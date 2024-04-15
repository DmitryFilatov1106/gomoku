package ru.fildv.gomoku.impl;

import ru.fildv.gomoku.model.CellValue;
import ru.fildv.gomoku.model.GameTable;
import ru.fildv.gomoku.properties.Properties;

public class GameTableImpl implements GameTable {
    private final CellValue[][] gameTable;

    public GameTableImpl() {
        gameTable = new CellValue[Properties.SIZE][Properties.SIZE];
        reInit();
    }

    @Override
    public CellValue getValue(final int row, final int col) {
        if (row >= 0 && row < getSize() && col >= 0 && col < getSize()) {
            return gameTable[row][col];
        } else {
            throw new IndexOutOfBoundsException("Invalid row or col: row = "
                    + row + ", col = " + col + ", size = " + getSize());
        }
    }

    @Override
    public void setValue(final int row,
                         final int col,
                         final CellValue cellValue) {
        if (row >= 0 && row < getSize() && col >= 0 && col < getSize()) {
            gameTable[row][col] = cellValue;
        } else {
            throw new IndexOutOfBoundsException("Invalid row or col: row = "
                    + row + ", col = " + col + ", size = " + getSize());
        }
    }

    @Override
    public void reInit() {
        for (int i = 0; i < getSize(); i++) {
            for (int j = 0; j < getSize(); j++) {
                setValue(i, j, CellValue.EMPTY);
            }
        }
    }

    @Override
    public int getSize() {
        return gameTable.length;
    }

    @Override
    public boolean isCellFree(final int row, final int col) {
        return getValue(row, col) == CellValue.EMPTY;
    }

    @Override
    public boolean emptyCellNotExists() {
        for (int i = 0; i < getSize(); i++) {
            for (int j = 0; j < getSize(); j++) {
                if (getValue(i, j) == CellValue.EMPTY) {
                    return false;
                }
            }
        }
        return true;
    }
}
