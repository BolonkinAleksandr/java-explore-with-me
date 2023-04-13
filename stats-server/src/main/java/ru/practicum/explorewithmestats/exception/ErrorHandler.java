package ru.practicum.explorewithmestats.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.time.LocalDateTime;
import java.util.Collections;

@ControllerAdvice
public class ErrorHandler {
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    public ApiError handleNotFoundResponse(NotFoundException e) {
        return new ApiError(Collections.emptyList(),
                e.getMessage(),
                "Object was not found",
                StatusError.NOT_FOUND,
                LocalDateTime.now());
    }
}
