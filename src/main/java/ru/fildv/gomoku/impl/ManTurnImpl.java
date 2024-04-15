package ru.fildv.gomoku.impl;

import ru.fildv.gomoku.model.Cell;
import ru.fildv.gomoku.model.CellValue;
import ru.fildv.gomoku.model.GameTable;
import ru.fildv.gomoku.model.ManTurn;

import java.util.Objects;

public class ManTurnImpl implements ManTurn {
    private GameTable gameTable;

    @Override
    public void setGameTable(final GameTable gameTable) {
        Objects.requireNonNull(gameTable, "Game table is null!");
        this.gameTable = gameTable;
    }

    @Override
    public Cell makeTurn(final int row, final int col) {
        gameTable.setValue(row, col, CellValue.MAN);
        return new Cell(row, col);
    }
}
