package ru.fildv.gomoku.model;

public interface CompTurn {
    void setGameTable(GameTable gameTable);

    Cell makeTurn();

    Cell makeFirstTurn();
}
