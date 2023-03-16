package com.task.note.service;

import com.task.note.dto.LikeUnlikeNoteDTO;
import com.task.note.dto.NoteRequestDTO;
import com.task.note.model.Note;

import java.util.List;

public interface NoteService {
    Note createNote(NoteRequestDTO noteRequestDTO);
    List<Note> getNotes();
    Note getNote(String id);
    List<Note> getNotesByCreator();
    Note editNote(String id, NoteRequestDTO noteRequestDTO);
    Note deleteNote(String id);
    Note addRemoveLike(String id, LikeUnlikeNoteDTO likeUnlikeNoteDTO);
}
