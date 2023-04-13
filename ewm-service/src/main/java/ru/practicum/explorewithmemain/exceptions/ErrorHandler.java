package ru.practicum.explorewithmemain.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;
import java.time.LocalDateTime;
import java.util.Collections;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    public ApiError handleRequestException(RequestException e) {
        return new ApiError(Collections.emptyList(),
                e.getMessage(),
                "Incorrect data was sent in the request",
                StatusError.BAD_REQUEST,
                LocalDateTime.now());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    public ApiError handleBadMethodArgumentException(MethodArgumentNotValidException e) {
        return new ApiError(Collections.emptyList(),
                e.getMessage(),
                "Incorrect data was sent in the request",
                StatusError.BAD_REQUEST,
                LocalDateTime.now());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    public ApiError handleConversionFailedResponseException(ConversionFailedException e) {
        return new ApiError(Collections.emptyList(),
                e.getMessage(),
                "Incorrect data was sent in the request",
                StatusError.BAD_REQUEST,
                LocalDateTime.now());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    public ApiError handleConstraintViolationException(ConstraintViolationException e) {
        return new ApiError(Collections.emptyList(),
                e.getMessage(),
                "Incorrect data was sent in the request",
                StatusError.BAD_REQUEST,
                LocalDateTime.now());
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler
    public ApiError handleForbiddenException(ForbiddenException e) {
        return new ApiError(Collections.emptyList(),
                e.getMessage(),
                "Incorrect data was sent in the request",
                StatusError.FORBIDDEN,
                LocalDateTime.now());
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler
    public ApiError handleNotFoundException(NotFoundException e) {
        return new ApiError(Collections.emptyList(),
                e.getMessage(),
                "Object was not found",
                StatusError.NOT_FOUND,
                LocalDateTime.now());
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler
    public ApiError handleDataIntegrityViolationException(DataIntegrityViolationException e) {
        return new ApiError(Collections.emptyList(),
                e.getMessage(),
                "Database exception",
                StatusError.CONFLICT,
                LocalDateTime.now());
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler
    public ApiError handleInternalServerException(RuntimeException e) {
        return new ApiError(Collections.emptyList(),
                e.getMessage(),
                "Internal server error",
                StatusError.INTERNAL_SERVER_ERROR,
                LocalDateTime.now());
    }
}
