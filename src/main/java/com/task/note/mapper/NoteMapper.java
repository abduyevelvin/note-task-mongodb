package com.task.note.mapper;

import com.task.note.dto.NoteResponseDTO;
import com.task.note.model.Note;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring")
public interface NoteMapper {
    NoteMapper INSTANCE = Mappers.getMapper(NoteMapper.class);
    @Mapping(source = "id", target = "noteId")
    @Mapping(source = "likedUsers", target = "numberOfLikes", qualifiedByName = "calculateNumberOfLikes")
    NoteResponseDTO noteToNoteResponseDTO(Note note);

    @Mapping(source = "id", target = "noteId")
    @Mapping(source = "likedUsers", target = "numberOfLikes", qualifiedByName = "calculateNumberOfLikes")
    List<NoteResponseDTO> notesToNoteResponseDTOs(List<Note> notes);

    @Named("calculateNumberOfLikes")
    static Integer calculateNumberOfLikes(Set<String> likedUsers) {
        return likedUsers.size();
    }
}
