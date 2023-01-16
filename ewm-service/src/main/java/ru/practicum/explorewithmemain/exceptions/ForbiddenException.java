package ru.practicum.explorewithmemain.exceptions;

public class ForbiddenException extends RuntimeException {

    public ForbiddenException(String message, String errorObject) {
        super("Only pending or canceled events can be changed" + message + " " + errorObject);
    }
}
