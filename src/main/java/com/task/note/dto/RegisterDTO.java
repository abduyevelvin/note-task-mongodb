package com.task.note.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.Set;

@Data
public class RegisterDTO {
    private String userId;
    @NotBlank
    private String username;
    @NotBlank
    private String password;
    @NotEmpty
    private Set<String> roles;

    private Set<String> likedNotes;
}
