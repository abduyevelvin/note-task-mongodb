package com.task.note.controller;

import com.task.note.dto.LikeUnlikeNoteDTO;
import com.task.note.dto.NoteRequestDTO;
import com.task.note.dto.ResponseDTO;
import com.task.note.mapper.NoteMapper;
import com.task.note.service.NoteService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static org.springframework.http.HttpStatus.*;

@RestController
@Validated
@RequestMapping("/notes")
@RequiredArgsConstructor
public class NoteController {
    private final NoteService noteService;
    private final NoteMapper noteMapper;

    @PostMapping("/")
    @ApiOperation(value = "returns created note", notes = "need to provide valid note in body for creation")
    public ResponseEntity<ResponseDTO<Object>> createNote(@Valid @RequestBody NoteRequestDTO noteRequestDTO) {
        var note = noteService.createNote(noteRequestDTO);
        var responseDTO = ResponseDTO.builder().responseMsg("Note created...")
                .data(noteMapper.noteToNoteResponseDTO(note)).build();

        return new ResponseEntity<>(responseDTO, CREATED);
    }

    @GetMapping("/")
    @ApiOperation("returns all notes from DB")
    public ResponseEntity<ResponseDTO<Object>> getNotes(){
        var notes = noteService.getNotes();
        var responseDTO = ResponseDTO.builder().responseMsg("Notes list...")
                .data(notes.isEmpty() ? "No Notes in the DB" : noteMapper.notesToNoteResponseDTOs(notes)).build();

        return new ResponseEntity<>(responseDTO, OK);
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "returns note by id or not found exception",
            notes = "need to provide existing note id")
    public ResponseEntity<ResponseDTO<Object>> getNote(@PathVariable("id") String id){
        var note = noteService.getNote(id);
        var responseDTO = ResponseDTO.builder().responseMsg("Note details...")
                .data(noteMapper.noteToNoteResponseDTO(note)).build();

        return new ResponseEntity<>(responseDTO, OK);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('USER')")
    @ApiOperation(value = "returns updated note or not found exception",
            notes = "need to provide valid note in body and existing note id to update a note")
    public ResponseEntity<ResponseDTO<Object>> editNote(@PathVariable("id") String id, @Valid @RequestBody NoteRequestDTO noteRequestDTO){
        var note = noteService.editNote(id, noteRequestDTO);
        var responseDTO = ResponseDTO.builder().responseMsg("Note edited...")
                .data(noteMapper.noteToNoteResponseDTO(note)).build();

        return new ResponseEntity<>(responseDTO, ACCEPTED);
    }

    @PutMapping("/like/{id}")
    @PreAuthorize("hasAuthority('USER')")
    @ApiOperation(value = "returns liked/unliked note or not found exception",
            notes = "need to provide valid like/unlike request body and existing note id to update a note")
    public ResponseEntity<ResponseDTO<Object>> likeNote(@PathVariable("id") String id, @RequestBody LikeUnlikeNoteDTO likeUnlikeNoteDTO){
        var note = noteService.addRemoveLike(id, likeUnlikeNoteDTO);
        var responseDTO = ResponseDTO.builder().responseMsg("Liked note details...")
                .data(noteMapper.noteToNoteResponseDTO(note)).build();

        return new ResponseEntity<>(responseDTO, ACCEPTED);
    }

    @DeleteMapping("/{Id}")
    @PreAuthorize("hasAuthority('USER')")
    @ApiOperation(value = "returns a deleted note or not found exception",
            notes = "need to provide existing note id")
    public ResponseEntity<ResponseDTO<Object>> deleteUser(@PathVariable("id") String id){
        var note = noteService.deleteNote(id);
        var responseDTO = ResponseDTO.builder().responseMsg("Deleted note...")
                .data(noteMapper.noteToNoteResponseDTO(note)).build();

        return new ResponseEntity<>(responseDTO, NO_CONTENT);
    }
}
