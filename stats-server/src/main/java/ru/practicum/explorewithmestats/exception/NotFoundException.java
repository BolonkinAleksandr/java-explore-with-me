package ru.practicum.explorewithmestats.exception;

public class NotFoundException extends Exception {

    public NotFoundException(String message, String errorObject) {

        super("The required object was not found." + message + " " + errorObject);
    }
}
