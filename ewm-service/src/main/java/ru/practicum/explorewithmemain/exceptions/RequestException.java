package ru.practicum.explorewithmemain.exceptions;

public class RequestException extends RuntimeException {

    public RequestException(String message, String errorObject) {
        super("Only pending or canceled events can be changed" + message + " " + errorObject);
    }
}
