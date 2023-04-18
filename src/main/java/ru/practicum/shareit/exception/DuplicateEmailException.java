package ru.practicum.shareit.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DuplicateEmailException extends IllegalArgumentException {
    public DuplicateEmailException(String message) {
        super(message);
        log.error(message);
    }
}
