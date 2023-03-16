package com.task.note.exception;

import lombok.Data;

import java.io.Serial;

@Data
public class APIAbstractException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;
    protected final Integer code;
    protected final String message;

    public APIAbstractException(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
