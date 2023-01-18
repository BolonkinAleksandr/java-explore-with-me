package ru.practicum.explorewithmemain.exceptions;

public class NotFoundException extends RuntimeException {

    public NotFoundException(String message, String errorObject) {

        super("object was not found" + message + " " + errorObject);
    }
}
