package com.task.note.exception;

import com.task.note.enums.APIErrorCode;
import com.task.note.exception.APIAbstractException;
import lombok.Data;

import java.io.Serial;

@Data
public class UserNotFoundException extends APIAbstractException {
    @Serial
    private static final long serialVersionUID = 1L;

    public UserNotFoundException(APIErrorCode APIErrorCode) {
        super(APIErrorCode.getCode(), APIErrorCode.getMessage().toLowerCase());
    }
}
