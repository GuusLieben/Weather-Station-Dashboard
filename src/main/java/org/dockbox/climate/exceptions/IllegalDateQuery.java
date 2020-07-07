package org.dockbox.climate.exceptions;

import java.time.LocalDate;

public class IllegalDateQuery extends RuntimeException {

    private final LocalDate before;
    private final LocalDate after;

    public LocalDate getBefore() {
        return before;
    }

    public LocalDate getAfter() {
        return after;
    }

    public IllegalDateQuery(LocalDate before, LocalDate after) {
        this.before = before;
        this.after = after;
    }

    public IllegalDateQuery(String message, LocalDate before, LocalDate after) {
        super(message);
        this.before = before;
        this.after = after;
    }

    public IllegalDateQuery(String message, Throwable cause, LocalDate before, LocalDate after) {
        super(message, cause);
        this.before = before;
        this.after = after;
    }

    public IllegalDateQuery(Throwable cause, LocalDate before, LocalDate after) {
        super(cause);
        this.before = before;
        this.after = after;
    }

    public IllegalDateQuery(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, LocalDate before, LocalDate after) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.before = before;
        this.after = after;
    }
}
