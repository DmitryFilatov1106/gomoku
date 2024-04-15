package ru.fildv.gomoku.exception;

import java.io.Serial;

public class ComputerCantMakeTurnException extends IllegalStateException {
    @Serial
    private static final long serialVersionUID = 1L;

    public ComputerCantMakeTurnException(final String message) {
        super(message);
    }
}
