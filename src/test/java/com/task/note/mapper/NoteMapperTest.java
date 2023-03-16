package com.task.note.mapper;

import com.task.note.model.Note;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class NoteMapperTest {
    @Autowired
    private NoteMapper noteMapper;

    @Test
    void notesToNoteResponseDTOsTest() {
        //given
        var note = Note.builder()
                .id("test-id")
                .description("test-description")
                .createdAt(LocalDateTime.now())
                .noteCreator("test-user")
                .likedUsers(Set.of("liked-user"))
                .build();

        var notes = List.of(note);

        //when
        var noteResponseDTOS = noteMapper.notesToNoteResponseDTOs(notes);

        //then
        assertSoftly(as -> {
            assertEquals(notes.size(), noteResponseDTOS.size());
            assertEquals(note.getId(), noteResponseDTOS.get(0).getNoteId());
            assertEquals(note.getDescription(), noteResponseDTOS.get(0).getDescription());
            assertEquals(note.getCreatedAt(), noteResponseDTOS.get(0).getCreatedAt());
            assertEquals(note.getNoteCreator(), noteResponseDTOS.get(0).getNoteCreator());
            assertEquals(note.getLikedUsers().size(), noteResponseDTOS.get(0).getNumberOfLikes());
        });
    }
}
