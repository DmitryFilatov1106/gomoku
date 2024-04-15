package ru.fildv.gomoku.model;

public interface ManTurn {
    void setGameTable(GameTable gameTable);

    Cell makeTurn(int row, int col);
}
