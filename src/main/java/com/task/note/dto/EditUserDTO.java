package com.task.note.dto;

import lombok.Data;

import java.util.Set;

@Data
public class EditUserDTO {
    private String password;
    private Set<String> roles;
}
