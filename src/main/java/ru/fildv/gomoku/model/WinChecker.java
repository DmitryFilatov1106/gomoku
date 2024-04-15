package ru.fildv.gomoku.model;

public interface WinChecker {
    void setGameTable(GameTable gameTable);

    WinResult isWinnerFound(CellValue cellValue);
}
