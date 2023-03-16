package com.task.note.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class NoteResponseDTO {
    private String noteId;
    private String description;
    private LocalDateTime createdAt;
    private String noteCreator;
    private Integer numberOfLikes;
}
