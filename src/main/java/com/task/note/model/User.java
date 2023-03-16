package com.task.note.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Document(collection = "User")
public class User {
    @Id
    private String id;

    private String username;

    private String password;

    private Set<String> roles;

    private Set<String> likedNotes;
}
