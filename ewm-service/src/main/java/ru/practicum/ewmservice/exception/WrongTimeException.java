package ru.practicum.ewmservice.exception;

public class WrongTimeException extends RuntimeException{
    public WrongTimeException(String message) {
        super(message);
    }
}
