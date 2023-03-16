package com.task.note.controller;

import com.task.note.auth.exception.InvalidCredentialsException;
import com.task.note.dto.ErrorDTO;
import com.task.note.exception.InvalidLikeUnlikeException;
import com.task.note.exception.NoteNotFoundException;
import com.task.note.exception.UserExistsException;
import com.task.note.exception.UserNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static org.springframework.http.HttpStatus.*;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ResponseStatus(value = BAD_REQUEST)
    @ExceptionHandler({MethodArgumentNotValidException.class})
    public Object handleException(MethodArgumentNotValidException exception) {
        var message = exception.getBindingResult().getFieldErrors().get(0).getField() + " - "
                + exception.getBindingResult().getFieldErrors().get(0).getDefaultMessage();
        return ErrorDTO.builder()
                .code(BAD_REQUEST.value())
                .message(message)
                .build();
    }

    @ResponseStatus(value = CONFLICT)
    @ExceptionHandler({UserExistsException.class})
    public Object handleException(UserExistsException exception) {
        return ErrorDTO.builder()
                .code(exception.getCode())
                .message(exception.getMessage())
                .build();
    }

    @ResponseStatus(value = NOT_FOUND)
    @ExceptionHandler({UserNotFoundException.class})
    public Object handleException(UserNotFoundException exception) {
        return ErrorDTO.builder()
                .code(exception.getCode())
                .message(exception.getMessage())
                .build();
    }

    @ResponseStatus(value = BAD_REQUEST)
    @ExceptionHandler({InvalidCredentialsException.class})
    public Object handleException(InvalidCredentialsException exception) {
        return ErrorDTO.builder()
                .code(exception.getCode())
                .message(exception.getMessage())
                .build();
    }

    @ResponseStatus(value = NOT_FOUND)
    @ExceptionHandler({NoteNotFoundException.class})
    public Object handleException(NoteNotFoundException exception) {
        return ErrorDTO.builder()
                .code(exception.getCode())
                .message(exception.getMessage())
                .build();
    }

    @ResponseStatus(value = CONFLICT)
    @ExceptionHandler({InvalidLikeUnlikeException.class})
    public Object handleException(InvalidLikeUnlikeException exception) {
        return ErrorDTO.builder()
                .code(exception.getCode())
                .message(exception.getMessage())
                .build();
    }
}
