package ru.fildv.gomoku.model;

public interface GameTable {
    CellValue getValue(int row, int col);

    void setValue(int row, int col, CellValue cellValue);

    void reInit();

    int getSize();

    boolean isCellFree(int row, int col);

    boolean emptyCellNotExists();
}
