package com.task.note.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class NoteRequestDTO {
    @NotBlank
    private String description;
}
