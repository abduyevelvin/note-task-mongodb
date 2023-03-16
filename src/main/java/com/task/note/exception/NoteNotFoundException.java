package com.task.note.exception;

import com.task.note.enums.APIErrorCode;
import lombok.Data;

import java.io.Serial;

@Data
public class NoteNotFoundException extends APIAbstractException {
    @Serial
    private static final long serialVersionUID = 1L;

    public NoteNotFoundException(APIErrorCode APIErrorCode) {
        super(APIErrorCode.getCode(), APIErrorCode.getMessage().toLowerCase());
    }
}
