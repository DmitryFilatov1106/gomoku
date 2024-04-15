package ru.fildv.gomoku.impl;

import ru.fildv.gomoku.model.Cell;
import ru.fildv.gomoku.model.CellValue;
import ru.fildv.gomoku.model.GameTable;
import ru.fildv.gomoku.model.WinChecker;
import ru.fildv.gomoku.model.WinResult;
import ru.fildv.gomoku.properties.Properties;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class WinCheckerImpl implements WinChecker {
    private final int winCount = Properties.WIN_COUNT;
    private GameTable gameTable;

    @Override
    public void setGameTable(final GameTable gameTable) {
        Objects.requireNonNull(gameTable, "Game table is null!");
        if (gameTable.getSize() < winCount) {
            throw new IllegalArgumentException("Game table is small: "
                    + gameTable.getSize() + ". Required >= " + winCount);
        }
        this.gameTable = gameTable;
    }

    @Override
    public WinResult isWinnerFound(final CellValue cellValue) {
        Objects.requireNonNull(cellValue, "Cell value is null!");
        List<Cell> result = isWinnerByRow(cellValue);
        if (result != null) {
            return new WinResultImpl(result);
        }
        result = isWinnerByCol(cellValue);
        if (result != null) {
            return new WinResultImpl(result);
        }
        result = isWinnerByDiagonal(cellValue);
        if (result != null) {
            return new WinResultImpl(result);
        }
        result = isWinnerByCrossDiagonal(cellValue);
        if (result != null) {
            return new WinResultImpl(result);
        }
        return new WinResultImpl(null);
    }

    private List<Cell> isWinnerByRow(final CellValue cellValue) {
        for (int i = 0; i < gameTable.getSize(); i++) {
            List<Cell> cells = new ArrayList<>();
            for (int j = 0; j < gameTable.getSize(); j++) {
                if (gameTable.getValue(i, j) == cellValue) {
                    cells.add(new Cell(i, j));
                    if (cells.size() == winCount) {
                        return cells;
                    }
                } else {
                    cells.clear();
                    if (j > gameTable.getSize() - winCount) {
                        break;
                    }
                }
            }
        }
        return null;
    }

    private List<Cell> isWinnerByCol(final CellValue cellValue) {
        for (int i = 0; i < gameTable.getSize(); i++) {
            List<Cell> cells = new ArrayList<>();
            for (int j = 0; j < gameTable.getSize(); j++) {
                if (gameTable.getValue(j, i) == cellValue) {
                    cells.add(new Cell(j, i));
                    if (cells.size() == winCount) {
                        return cells;
                    }
                } else {
                    cells.clear();
                    if (j > gameTable.getSize() - winCount) {
                        break;
                    }
                }
            }
        }
        return null;
    }

    private List<Cell> isWinnerByDiagonal(final CellValue cellValue) {
        int winDelta = winCount - 1;
        for (int i = 0; i < gameTable.getSize() - winDelta; i++) {
            for (int j = 0; j < gameTable.getSize() - winDelta; j++) {
                List<Cell> cells = new ArrayList<>();
                for (int k = 0; k < winCount; k++) {
                    if (gameTable.getValue(i + k, j + k) == cellValue) {
                        cells.add(new Cell(i + k, j + k));
                        if (cells.size() == winCount) {
                            return cells;
                        }
                    } else {
                        break;
                    }
                }
            }
        }
        return null;
    }

    private List<Cell> isWinnerByCrossDiagonal(final CellValue cellValue) {
        int winDelta = winCount - 1;
        for (int i = 0; i < gameTable.getSize() - winDelta; i++) {
            for (int j = winDelta; j < gameTable.getSize(); j++) {
                List<Cell> cells = new ArrayList<>();
                for (int k = 0; k < winCount; k++) {
                    if (gameTable.getValue(i + k, j - k) == cellValue) {
                        cells.add(new Cell(i + k, j - k));
                        if (cells.size() == winCount) {
                            return cells;
                        }
                    } else {
                        break;
                    }
                }
            }
        }
        return null;
    }

    private static class WinResultImpl implements WinResult {
        private final List<Cell> winCells;

        WinResultImpl(final List<Cell> winCells) {
            this.winCells = winCells == null ? new ArrayList<>() : winCells;
        }

        @Override
        public boolean winnerExists() {
            return winCells.size() > 0;
        }

        @Override
        public List<Cell> getWinnerCells() {
            return winCells;
        }
    }
}
