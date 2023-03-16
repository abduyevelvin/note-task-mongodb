package com.task.note.enums;

import lombok.Getter;

@Getter
public enum APIErrorCode {

    USER_EXISTS(111, "user_exists"),
    USER_NOT_FOUND(112, "user_not_found"),
    INVALID_CREDENTIALS(113, "invalid_credentials"),
    NOTE_NOT_FOUND(221, "note_not_found"),
    SAME_NOTE_LIKE_NOT_ALLOWED(222, "multiple_like_not_allowed"),
    INVALID_UNLIKE_NOT_LIKED_NOTE(223, "cannot_unlike_not_liked_note");

    private Integer code;
    private String message;

    APIErrorCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
