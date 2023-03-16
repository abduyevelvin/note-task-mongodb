package com.task.note.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Document(collection = "Note")
public class Note {
    @Id
    private String id;

    private String description;

    private LocalDateTime createdAt;

    private String noteCreator;

    private Set<String> likedUsers;
}
