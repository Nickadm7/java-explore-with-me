package ru.practicum.ewmservice.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@RestControllerAdvice
public class ErrorHandler {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public Map<String, String> handleConflictException(final ConflictException e) {
        String error = HttpStatus.CONFLICT + " " + LocalDateTime.now().format(formatter);
        return Map.of(error, e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleNotFoundParameterException(final NotFoundParameterException e) {
        String error = HttpStatus.BAD_REQUEST + " " + LocalDateTime.now().format(formatter);
        return Map.of(error, e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public Map<String, String> handleValidationException(final ValidationException e) {
        String error = HttpStatus.CONFLICT + " " + LocalDateTime.now().format(formatter);
        return Map.of(error, e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public Map<String, String> handleWrongTimeException(final WrongTimeException e) {
        String error = HttpStatus.CONFLICT + " " + LocalDateTime.now().format(formatter);
        return Map.of(error, e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public Map<String, String> handleWrongUpdatedEventException(final WrongUpdatedEventException e) {
        String error = HttpStatus.CONFLICT + " " + LocalDateTime.now().format(formatter);
        return Map.of(error, e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleDataIntegrityViolationException(final DataIntegrityViolationException e) {
        return ApiError.builder()
                .errors(HttpStatus.BAD_REQUEST.toString())
                .message(HttpStatus.BAD_REQUEST.toString())
                .reason(HttpStatus.BAD_REQUEST.toString())
                .status(HttpStatus.BAD_REQUEST)
                .timestamp(LocalDateTime.now())
                .build();
    }
}