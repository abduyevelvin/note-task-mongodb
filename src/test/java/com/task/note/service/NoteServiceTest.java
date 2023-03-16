package com.task.note.service;

import com.task.note.model.User;
import com.task.note.repository.UserRepository;
import com.task.note.auth.service.impl.UserDetailsImpl;
import com.task.note.dto.LikeUnlikeNoteDTO;
import com.task.note.dto.NoteRequestDTO;
import com.task.note.exception.InvalidLikeUnlikeException;
import com.task.note.model.Note;
import com.task.note.repository.NoteRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
public class NoteServiceTest {

    private static final String NOTE_ID = "Note-1";
    private static final String NOTE_DESCRIPTION = "Note-Description";
    private static final LocalDateTime NOTE_CREATED_AT = LocalDateTime.now();
    private static final String NOTE_CREATOR = "NOTE-CREATOR";
    private static final String NOTE_LIKED_USER = "NOTE-LIKER";
    private static final String USERNAME = "USERNAME";


    @Autowired
    private NoteService noteService;

    @MockBean
    private NoteRepository noteRepositoryMock;

    @MockBean
    private UserRepository userRepositoryMock;

    @Test
    void createNoteTest() {
        //given
        var note = createNote();
        mockAuthentication();
        when(noteRepositoryMock.save(any())).thenReturn(note);

        //when
        var createdNote = noteService.createNote(createNoteRequestDTO());

        //then
        assertSingleNoteTestResults(note, createdNote);
    }

    @Test
    void getAllNotesTest() {
        //given
        var note = createNote();
        var notes = List.of(note);
        when(noteRepositoryMock.findAll(Sort.by(Sort.Direction.DESC, "createdAt"))).thenReturn(notes);

        //when
        var allNotes = noteService.getNotes();

        //then
        assertListNotesTestResults(notes, allNotes);
    }

    @Test
    void getNoteByIDTest() {
        //given
        var note = createNote();
        when(noteRepositoryMock.findById(NOTE_ID)).thenReturn(Optional.of(note));

        //when
        var serviceNote = noteService.getNote(NOTE_ID);

        //then
        assertSingleNoteTestResults(note, serviceNote);
    }

    @Test
    void getAllNotesByCreatorTest() {
        //given
        var note = createNote();
        var notes = List.of(note);
        mockAuthentication();
        when(noteRepositoryMock.findAllByNoteCreator(USERNAME)).thenReturn(notes);

        //when
        var allNotesByCreator = noteService.getNotesByCreator();

        //then
        assertListNotesTestResults(notes, allNotesByCreator);
    }

    @Test
    void editNoteTest() {
        //given
        var note = createNote();
        when(noteRepositoryMock.findById(NOTE_ID)).thenReturn(Optional.of(note));
        var noteRequestDTO = new NoteRequestDTO();
        noteRequestDTO.setDescription("edited description");
        note.setDescription(noteRequestDTO.getDescription());
        when(noteRepositoryMock.save(any())).thenReturn(note);

        //when
        var editedNote = noteService.editNote(NOTE_ID, noteRequestDTO);

        //then
        assertSingleNoteTestResults(note, editedNote);
    }

    @Test
    void deleteNoteTest() {
        //given
        var note = createNote();
        mockAuthentication();
        when(noteRepositoryMock.findById(NOTE_ID)).thenReturn(Optional.of(note));
        when(userRepositoryMock.findByUsername(USERNAME)).thenReturn(Optional.of(createUser()));
        when(userRepositoryMock.save(any())).thenReturn(createUser());

        //when
        var deletedNote = noteService.deleteNote(NOTE_ID);

        //then
        assertSingleNoteTestResults(note, deletedNote);
    }

    @Test
    void addRemoveLikeTest() {
        //given
        var note = createNote();
        mockAuthentication();
        when(noteRepositoryMock.findById(NOTE_ID)).thenReturn(Optional.of(note));
        when(noteRepositoryMock.save(any())).thenReturn(note);
        when(userRepositoryMock.findByUsername(USERNAME)).thenReturn(Optional.of(createUser()));
        when(userRepositoryMock.save(any())).thenReturn(createUser());

        //when
        var likedNote = noteService.addRemoveLike(NOTE_ID, createLikeUnlikeNoteDTO(true));

        //then
        assertSingleNoteTestResults(note, likedNote);
    }

    @Test
    void addRemoveLikeTestThrowException() {
        //given
        var note = createNote();
        mockAuthentication();
        when(noteRepositoryMock.findById(NOTE_ID)).thenReturn(Optional.of(note));
        when(noteRepositoryMock.save(any())).thenReturn(note);
        when(userRepositoryMock.findByUsername(USERNAME)).thenReturn(Optional.of(createUser()));
        when(userRepositoryMock.save(any())).thenReturn(createUser());

        //when - then
        assertThrows(InvalidLikeUnlikeException.class,
                () -> noteService.addRemoveLike(NOTE_ID, createLikeUnlikeNoteDTO(false)));
    }

    private Note createNote() {
        Set<String> likedUsers = new HashSet<>();
        likedUsers.add(NOTE_LIKED_USER);

        return Note.builder()
                .id(NOTE_ID)
                .description(NOTE_DESCRIPTION)
                .createdAt(NOTE_CREATED_AT)
                .noteCreator(NOTE_CREATOR)
                .likedUsers(likedUsers)
                .build();
    }

    private NoteRequestDTO createNoteRequestDTO() {
        var noteRequestDTO = new NoteRequestDTO();
        noteRequestDTO.setDescription(NOTE_DESCRIPTION);

        return noteRequestDTO;
    }

    private User createUser() {
        Set<String> likedNotes = new HashSet<>();

        return User.builder()
                .id("id")
                .username(USERNAME)
                .password("password")
                .roles(Set.of("user"))
                .likedNotes(likedNotes)
                .build();
    }

    private LikeUnlikeNoteDTO createLikeUnlikeNoteDTO(boolean like) {
        var likeUnlikeNoteDTO = new LikeUnlikeNoteDTO();
        likeUnlikeNoteDTO.setLike(like);

        return likeUnlikeNoteDTO;
    }

    private void mockAuthentication() {
        var auth = mock(Authentication.class);

        when(auth.getPrincipal()).thenReturn(UserDetailsImpl.build(createUser()));

        var securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(auth);
        SecurityContextHolder.setContext(securityContext);
    }

    private void assertSingleNoteTestResults(Note expectedNote, Note actualNote) {
        assertSoftly(as -> {
            assertEquals(expectedNote.getId(), actualNote.getId());
            assertEquals(expectedNote.getDescription(), actualNote.getDescription());
            assertEquals(expectedNote.getNoteCreator(), actualNote.getNoteCreator());
            assertEquals(expectedNote.getCreatedAt(), actualNote.getCreatedAt());
            assertEquals(expectedNote.getLikedUsers().size(), actualNote.getLikedUsers().size());
            assertTrue(expectedNote.getLikedUsers().containsAll(actualNote.getLikedUsers()));
        });
    }

    private void assertListNotesTestResults(List<Note> expectedNotes, List<Note> actualNotes) {
        assertSoftly(as -> {
            assertEquals(expectedNotes.size(), actualNotes.size());
            assertEquals(expectedNotes.get(0).getId(), actualNotes.get(0).getId());
            assertEquals(expectedNotes.get(0).getDescription(), actualNotes.get(0).getDescription());
            assertEquals(expectedNotes.get(0).getNoteCreator(), actualNotes.get(0).getNoteCreator());
            assertEquals(expectedNotes.get(0).getCreatedAt(), actualNotes.get(0).getCreatedAt());
            assertEquals(expectedNotes.get(0).getLikedUsers().size(), actualNotes.get(0).getLikedUsers().size());
            assertTrue(expectedNotes.get(0).getLikedUsers().containsAll(actualNotes.get(0).getLikedUsers()));
        });
    }
}
