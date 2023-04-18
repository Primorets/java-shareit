package ru.practicum.shareit.handler;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.shareit.exception.ItemNotFoundException;
import ru.practicum.shareit.exception.DuplicateEmailException;
import ru.practicum.shareit.exception.UserNotFoundException;
import ru.practicum.shareit.exception.ValidationException;

@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handlerUserNotFound(final UserNotFoundException userNotFoundException) {
        return new ErrorResponse(userNotFoundException.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handlerItemNotFound(final ItemNotFoundException itemNotFoundException) {
        return new ErrorResponse(itemNotFoundException.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handlerValidationException(ValidationException validationException) {
        return new ErrorResponse(validationException.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handlerDuplicateEmailException(DuplicateEmailException duplicateEmailException) {
        return new ErrorResponse(duplicateEmailException.getMessage());
    }
}
