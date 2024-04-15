package ru.fildv.gomoku;

import ru.fildv.gomoku.impl.CompTurnImpl;
import ru.fildv.gomoku.impl.GameTableImpl;
import ru.fildv.gomoku.impl.ManTurnImpl;
import ru.fildv.gomoku.impl.WinCheckerImpl;
import ru.fildv.gomoku.model.Cell;
import ru.fildv.gomoku.model.CellValue;
import ru.fildv.gomoku.model.CompTurn;
import ru.fildv.gomoku.model.GameTable;
import ru.fildv.gomoku.model.ManTurn;
import ru.fildv.gomoku.model.WinChecker;
import ru.fildv.gomoku.model.WinResult;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.Serial;


public class Main extends JFrame {
    @Serial
    private static final long serialVersionUID = 1L;
    private final JLabel[][] cells;
    private final GameTable gameTable;
    private final ManTurn humanTurn;
    private final CompTurn computerTurn;
    private final WinChecker winnerChecker;
    private boolean isHumanFirstTurn;

    public Main() throws HeadlessException {
        super("Gomoku");
        //config section
        gameTable = new GameTableImpl();
        humanTurn = new ManTurnImpl();
        computerTurn = new CompTurnImpl();
        winnerChecker = new WinCheckerImpl();
        //end config section
        initGameComponents();
        cells = new JLabel[gameTable.getSize()][gameTable.getSize()];
        isHumanFirstTurn = true;
        createGameUITable();
    }

    public static void main(final String[] args) {
        Main w = new Main();
        w.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        w.setResizable(false);
        w.pack();
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        w.setLocation(dim.width / 2 - w.getSize().width / 2,
                dim.height / 2 - w.getSize().height / 2);
        w.setVisible(true);
    }

    protected void initGameComponents() {
        humanTurn.setGameTable(gameTable);
        computerTurn.setGameTable(gameTable);
        winnerChecker.setGameTable(gameTable);
    }

    protected void drawCellValue(final Cell cell) {
        CellValue cellValue = gameTable.getValue(cell.getRowIndex(),
                cell.getColIndex());
        cells[cell.getRowIndex()][cell.getColIndex()].setText(
                cellValue.getValue());
        if (cellValue == CellValue.COMP) {
            cells[cell.getRowIndex()][cell.getColIndex()].setForeground(
                    Color.RED);
        } else {
            //human
            cells[cell.getRowIndex()][cell.getColIndex()].setForeground(
                    Color.BLUE);
        }

    }

    protected void markWinnerCells(final java.util.List<Cell> winnerCells) {
        for (Cell cell : winnerCells) {
            cells[cell.getRowIndex()][cell.getColIndex()].setForeground(
                    Color.CYAN);
            cells[cell.getRowIndex()][cell.getColIndex()].setFont(
                    new Font(Font.SERIF, Font.BOLD, 35));
        }
    }

    protected void createGameUITable() {
        setLayout(new GridLayout(gameTable.getSize(), gameTable.getSize()));
        for (int i = 0; i < gameTable.getSize(); i++) {
            for (int j = 0; j < gameTable.getSize(); j++) {
                final int row = i;
                final int col = j;
                cells[i][j] = new JLabel();
                cells[i][j].setPreferredSize(new Dimension(45, 45));
                cells[i][j].setHorizontalAlignment(SwingConstants.CENTER);
                cells[i][j].setVerticalAlignment(SwingConstants.CENTER);
                cells[i][j].setFont(new Font(Font.SERIF, Font.PLAIN, 35));
                cells[i][j].setForeground(Color.BLACK);
                cells[i][j].setBorder(
                        BorderFactory.createLineBorder(Color.BLACK));
                add(cells[i][j]);
                cells[i][j].addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(final MouseEvent e) {
                        handleHumanTurn(row, col);
                    }
                });
            }
        }
    }

    protected void startNewGame() {
        isHumanFirstTurn = !isHumanFirstTurn;
        gameTable.reInit();
        for (int i = 0; i < gameTable.getSize(); i++) {
            for (int j = 0; j < gameTable.getSize(); j++) {
                cells[i][j].setText(gameTable.getValue(i, j).getValue());
                cells[i][j].setFont(new Font(Font.SERIF, Font.PLAIN, 35));
                cells[i][j].setForeground(Color.BLACK);
            }
        }
        if (!isHumanFirstTurn) {
            Cell compCell = computerTurn.makeFirstTurn();
            drawCellValue(compCell);
        }
    }

    protected void stopGame() {
        for (int i = 0; i < gameTable.getSize(); i++) {
            for (int j = 0; j < gameTable.getSize(); j++) {
                cells[i][j].removeMouseListener(cells[i][j]
                        .getMouseListeners()[0]);
            }
        }
    }

    protected void handleGameOver(final String message) {
        if (JOptionPane.showConfirmDialog(this, message)
                == JOptionPane.YES_OPTION) {
            startNewGame();
        } else {
            stopGame();
        }
    }

    protected void handleHumanTurn(final int row, final int col) {
        if (gameTable.isCellFree(row, col)) {
            Cell humanCell = humanTurn.makeTurn(row, col);
            drawCellValue(humanCell);
            WinResult winnerResult = winnerChecker.isWinnerFound(CellValue.MAN);
            if (winnerResult.winnerExists()) {
                markWinnerCells(winnerResult.getWinnerCells());
                handleGameOver("Game over: User win!\nNew game?");
                return;
            }
            if (gameTable.emptyCellNotExists()) {
                handleGameOver("Game over: Draw!\nNew game?");
                return;
            }
            Cell compCell = computerTurn.makeTurn();
            drawCellValue(compCell);
            winnerResult = winnerChecker.isWinnerFound(CellValue.COMP);
            if (winnerResult.winnerExists()) {
                markWinnerCells(winnerResult.getWinnerCells());
                handleGameOver("Game over: Computer wins!\n"
                        + "Do you want a new game?");
                return;
            }
            if (gameTable.emptyCellNotExists()) {
                handleGameOver("Game over: Draw!\nDo you want a new game?");
            }
        } else {
            JOptionPane.showMessageDialog(this,
                    "Cell is not free! Click on free cell!");
        }
    }
}
