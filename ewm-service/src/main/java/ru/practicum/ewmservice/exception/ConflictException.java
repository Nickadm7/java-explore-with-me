package ru.practicum.ewmservice.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ConflictException extends RuntimeException {
    public ConflictException(String message) {
        super(message);
        log.info("Ошибка валидации" + message);
    }
}
