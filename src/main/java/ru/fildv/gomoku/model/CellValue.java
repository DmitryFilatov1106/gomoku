package ru.fildv.gomoku.model;

public enum CellValue {
    EMPTY(' '),
    MAN('X'),
    COMP('O');
    private final char value;

    CellValue(final char value) {
        this.value = value;
    }

    public String getValue() {
        return String.valueOf(value);
    }
}
