package com.task.note.exception;

import com.task.note.enums.APIErrorCode;
import lombok.Data;

import java.io.Serial;

@Data
public class InvalidLikeUnlikeException extends APIAbstractException {
    @Serial
    private static final long serialVersionUID = 1L;

    public InvalidLikeUnlikeException(APIErrorCode APIErrorCode) {
        super(APIErrorCode.getCode(), APIErrorCode.getMessage().toLowerCase());
    }
}
