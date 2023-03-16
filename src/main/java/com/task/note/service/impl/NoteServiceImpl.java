package com.task.note.service.impl;

import com.task.note.auth.service.impl.UserDetailsImpl;
import com.task.note.dto.LikeUnlikeNoteDTO;
import com.task.note.dto.NoteRequestDTO;
import com.task.note.enums.APIErrorCode;
import com.task.note.exception.InvalidLikeUnlikeException;
import com.task.note.exception.NoteNotFoundException;
import com.task.note.model.Note;
import com.task.note.model.User;
import com.task.note.repository.NoteRepository;
import com.task.note.repository.UserRepository;
import com.task.note.service.NoteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

import static java.util.Collections.emptySet;

@Slf4j
@Service
@RequiredArgsConstructor
public class NoteServiceImpl implements NoteService {
    private final NoteRepository noteRepository;
    private final UserRepository userRepository;

    @Override
    public Note createNote(NoteRequestDTO noteRequestDTO) {
        var note = Note.builder()
                .description(noteRequestDTO.getDescription())
                .createdAt(LocalDateTime.now())
                .noteCreator(getUsername())
                .likedUsers(emptySet())
                .build();

        log.info("Saving note to the DB...");
        return noteRepository.save(note);
    }

    @Override
    public List<Note> getNotes() {
        log.info("Fetching all notes from DB sorted by creation date...");
        return noteRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt"));
    }

    @Override
    public Note getNote(String id) {
        log.info(String.format("Fetching note by id: %s", id));
        return noteRepository.findById(id)
                .orElseThrow(() -> {
                    log.error(String.format("Note not found with id: %s", id));
                    return new NoteNotFoundException(APIErrorCode.NOTE_NOT_FOUND);
                });
    }

    @Override
    public List<Note> getNotesByCreator() {
        log.info(String.format("Fetching notes by username: %s", getUsername()));
        return noteRepository.findAllByNoteCreator(getUsername());
    }

    @Override
    public Note editNote(String id, NoteRequestDTO noteRequestDTO) {
        var note = noteRepository.findById(id)
                .orElseThrow(() -> {
                    log.error(String.format("Note not found with id: %s", id));
                    return new NoteNotFoundException(APIErrorCode.NOTE_NOT_FOUND);
                });

        note.setDescription(noteRequestDTO.getDescription());

        log.info(String.format("Updating note with id: %s", id));
        return noteRepository.save(note);
    }

    @Override
    public Note deleteNote(String id) {
        var note = noteRepository.findById(id)
                .orElseThrow(() -> {
                    log.error(String.format("Note not found with id: %s", id));
                    return new NoteNotFoundException(APIErrorCode.NOTE_NOT_FOUND);
                });

        log.info(String.format("Fetching user by username: %s", getUsername()));
        var user = userRepository.findByUsername(getUsername()).get();

        log.info(String.format("Deleting note with id: %s", id));
        noteRepository.delete(note);

        log.info(String.format("Removing note from liked notes of user: %s", id));
        user.getLikedNotes().remove(note.getId());

        log.info("Saving user to the DB...");
        userRepository.save(user);

        return note;
    }

    @Override
    public Note addRemoveLike(String id, LikeUnlikeNoteDTO likeUnlikeNoteDTO) {
        var note = noteRepository.findById(id)
                .orElseThrow(() -> {
                    log.error(String.format("Note not found with id: %s", id));
                    return new NoteNotFoundException(APIErrorCode.NOTE_NOT_FOUND);
                });

        log.info(String.format("Fetching user by username: %s", getUsername()));
        var user = userRepository.findByUsername(getUsername()).get();

        if (likeUnlikeNoteDTO.isLike()) {
            addLike(note, user);
        } else {
            removeLike(note, user);
        }

        log.info("Saving user to the DB...");
        userRepository.save(user);

        log.info("Saving note to the DB...");
        return noteRepository.save(note);
    }

    private void addLike(Note note, User user) {
        if (note.getLikedUsers().contains(getUsername())) {
            log.error(String.format("User: '%s' cannot like same note: %s", user.getUsername(), note.getId()));
            throw new InvalidLikeUnlikeException(APIErrorCode.SAME_NOTE_LIKE_NOT_ALLOWED);
        }

        log.info(String.format("User: '%s' is liked the note: %s", user.getUsername(), note.getId()));
        note.getLikedUsers().add(getUsername());

        log.info(String.format("Note: '%s' is added to the user's: '%s' liked notes", note.getId(), user.getUsername()));
        user.getLikedNotes().add(note.getId());
    }

    private void removeLike(Note note, User user) {
        if (!note.getLikedUsers().contains(getUsername())) {
            log.error(String.format("User: '%s' cannot unlike not liked note: %s", user.getUsername(), note.getId()));
            throw new InvalidLikeUnlikeException(APIErrorCode.INVALID_UNLIKE_NOT_LIKED_NOTE);
        }

        log.info(String.format("User: '%s' is unliked the note: %s", user.getUsername(), note.getId()));
        note.getLikedUsers().remove(getUsername());

        log.info(String.format("Note: '%s' is removed from the user's: '%s' liked notes", note.getId(), user.getUsername()));
        user.getLikedNotes().remove(note.getId());
    }

    private String getUsername() {
        var user = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (!user.equals("anonymousUser")) {
            UserDetailsImpl userDetails = (UserDetailsImpl) user;
            return userDetails.getUsername();
        }

        return user.toString();
    }
}
