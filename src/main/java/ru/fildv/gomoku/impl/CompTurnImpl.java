package ru.fildv.gomoku.impl;

import ru.fildv.gomoku.exception.ComputerCantMakeTurnException;
import ru.fildv.gomoku.model.Cell;
import ru.fildv.gomoku.model.CellValue;
import ru.fildv.gomoku.model.CompTurn;
import ru.fildv.gomoku.model.GameTable;
import ru.fildv.gomoku.properties.Properties;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class CompTurnImpl implements CompTurn {
    private final int winCount = Properties.WIN_COUNT;
    private GameTable gameTable;

    @Override
    public void setGameTable(final GameTable gameTable) {
        Objects.requireNonNull(gameTable, "Game table can't be null");
        if (gameTable.getSize() < winCount) {
            throw new IllegalArgumentException(
                    "Size of gameTable is small: size=" + gameTable.getSize()
                            + ". Required >= " + winCount);
        }
        this.gameTable = gameTable;
    }

    @Override
    public Cell makeTurn() {
        CellValue[] figures = {CellValue.COMP, CellValue.MAN};
        for (int i = winCount - 1; i > 0; i--) {
            for (CellValue cellValue : figures) {
                Cell cell = tryMakeTurn(cellValue, i);
                if (cell != null) {
                    return cell;
                }
            }
        }
        return makeRandomTurn();
    }

    @Override
    public Cell makeFirstTurn() {
        Cell cell = new Cell(gameTable.getSize() / 2, gameTable.getSize() / 2);
        gameTable.setValue(
                cell.getRowIndex(), cell.getColIndex(), CellValue.COMP);
        return cell;
    }

    protected Cell makeRandomTurn() {
        List<Cell> emptyCells = getAllEmptyCells();
        if (emptyCells.size() > 0) {
            Cell randomCell = emptyCells.get(
                    new Random().nextInt(emptyCells.size()));
            gameTable.setValue(
                    randomCell.getRowIndex(),
                    randomCell.getColIndex(),
                    CellValue.COMP);
            return randomCell;
        } else {
            throw new ComputerCantMakeTurnException("All cells are filled!"
                    + "Have you checked draw state before "
                    + "call of computer turn?");
        }
    }

    protected List<Cell> getAllEmptyCells() {
        List<Cell> emptyCells = new ArrayList<>();
        for (int i = 0; i < gameTable.getSize(); i++) {
            for (int j = 0; j < gameTable.getSize(); j++) {
                if (gameTable.isCellFree(i, j)) {
                    emptyCells.add(new Cell(i, j));
                }
            }
        }
        return emptyCells;
    }

    protected Cell tryMakeTurn(final CellValue cellValue,
                               final int notBlankCount) {
        Cell cell = tryMakeTurnByRow(cellValue, notBlankCount);
        if (cell != null) {
            return cell;
        }
        cell = tryMakeTurnByCol(cellValue, notBlankCount);
        if (cell != null) {
            return cell;
        }
        cell = tryMakeTurnByMainDiagonal(cellValue, notBlankCount);
        if (cell != null) {
            return cell;
        }
        cell = tryMakeTurnByNotMainDiagonal(cellValue, notBlankCount);
        return cell;
    }

    protected Cell tryMakeTurnByRow(final CellValue cellValue,
                                    final int notBlankCount) {
        for (int i = 0; i < gameTable.getSize(); i++) {
            for (int j = 0; j < gameTable.getSize() - winCount - 1; j++) {
                boolean hasEmptyCells = false;
                int count = 0;
                List<Cell> inspectedCells = new ArrayList<>();
                for (int k = 0; k < winCount; k++) {
                    inspectedCells.add(new Cell(i, j + k));
                    if (gameTable.getValue(i, j + k) == cellValue) {
                        count++;
                    } else if (gameTable.getValue(i, j + k)
                            == CellValue.EMPTY) {
                        hasEmptyCells = true;
                    } else {
                        hasEmptyCells = false;
                        break;
                    }
                }
                if (count == notBlankCount && hasEmptyCells) {
                    return makeTurnToOneCellFromDataSet(inspectedCells);
                }
            }
        }
        return null;
    }

    protected Cell tryMakeTurnByCol(final CellValue cellValue,
                                    final int notBlankCount) {
        for (int i = 0; i < gameTable.getSize(); i++) {
            for (int j = 0; j < gameTable.getSize() - winCount - 1; j++) {
                boolean hasEmptyCells = false;
                int count = 0;
                List<Cell> inspectedCells = new ArrayList<>();
                for (int k = 0; k < winCount; k++) {
                    inspectedCells.add(new Cell(j + k, i));
                    if (gameTable.getValue(j + k, i) == cellValue) {
                        count++;
                    } else if (gameTable.getValue(j + k, i)
                            == CellValue.EMPTY) {
                        hasEmptyCells = true;
                    } else {
                        hasEmptyCells = false;
                        break;
                    }
                }
                if (count == notBlankCount && hasEmptyCells) {
                    return makeTurnToOneCellFromDataSet(inspectedCells);
                }
            }
        }
        return null;
    }

    protected Cell tryMakeTurnByMainDiagonal(final CellValue cellValue,
                                             final int notBlankCount) {
        for (int i = 0; i < gameTable.getSize() - winCount - 1; i++) {
            for (int j = 0; j < gameTable.getSize() - winCount - 1; j++) {
                boolean hasEmptyCells = false;
                int count = 0;
                List<Cell> inspectedCells = new ArrayList<>();
                for (int k = 0; k < winCount; k++) {
                    inspectedCells.add(new Cell(i + k, j + k));
                    if (gameTable.getValue(i + k, j + k) == cellValue) {
                        count++;
                    } else if (gameTable.getValue(i + k, j + k)
                            == CellValue.EMPTY) {
                        hasEmptyCells = true;
                    } else {
                        hasEmptyCells = false;
                        break;
                    }
                }
                if (count == notBlankCount && hasEmptyCells) {
                    return makeTurnToOneCellFromDataSet(inspectedCells);
                }
            }
        }
        return null;
    }

    protected Cell tryMakeTurnByNotMainDiagonal(final CellValue cellValue,
                                                final int notBlankCount) {
        for (int i = 0; i < gameTable.getSize() - winCount - 1; i++) {
            for (int j = winCount - 1; j < gameTable.getSize(); j++) {
                boolean hasEmptyCells = false;
                int count = 0;
                List<Cell> inspectedCells = new ArrayList<>();
                for (int k = 0; k < winCount; k++) {
                    inspectedCells.add(new Cell(i + k, j - k));
                    if (gameTable.getValue(i + k, j - k) == cellValue) {
                        count++;
                    } else if (gameTable.getValue(i + k, j - k)
                            == CellValue.EMPTY) {
                        hasEmptyCells = true;
                    } else {
                        hasEmptyCells = false;
                        break;
                    }
                }
                if (count == notBlankCount && hasEmptyCells) {
                    return makeTurnToOneCellFromDataSet(inspectedCells);
                }
            }
        }
        return null;
    }

    protected Cell makeTurnToOneCellFromDataSet(
            final List<Cell> inspectedCells) {
        Cell cell = findEmptyCellForComputerTurn(inspectedCells);
        gameTable.setValue(cell.getRowIndex(),
                cell.getColIndex(),
                CellValue.COMP);
        return cell;
    }

    protected Cell findEmptyCellForComputerTurn(final List<Cell> cells) {
        for (int i = 0; i < cells.size(); i++) {
            Cell currentCell = cells.get(i);
            if (gameTable.getValue(
                    currentCell.getRowIndex(),
                    currentCell.getColIndex()) != CellValue.EMPTY) {
                if (i == 0) {
                    if (isCellEmpty(cells.get(i + 1))) {
                        return cells.get(i + 1);
                    }
                } else if (i == cells.size() - 1) {
                    if (isCellEmpty(cells.get(i - 1))) {
                        return cells.get(i - 1);
                    }
                } else {
                    boolean searchDirectionAsc = new Random().nextBoolean();
                    int first = searchDirectionAsc ? i + 1 : i - 1;
                    int second = searchDirectionAsc ? i - 1 : i + 1;
                    if (isCellEmpty(cells.get(first))) {
                        return cells.get(first);
                    } else if (isCellEmpty(cells.get(second))) {
                        return cells.get(second);
                    }
                }
            }
        }
        throw new ComputerCantMakeTurnException(
                "All cells are filled: " + cells);
    }

    protected boolean isCellEmpty(final Cell cell) {
        return gameTable.getValue(
                cell.getRowIndex(), cell.getColIndex()) == CellValue.EMPTY;
    }
}
