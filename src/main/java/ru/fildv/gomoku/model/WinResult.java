package ru.fildv.gomoku.model;

import java.util.List;

public interface WinResult {
    boolean winnerExists();

    List<Cell> getWinnerCells();
}
