package com.task.note.mapper;

import com.task.note.dto.RegisterDTO;
import com.task.note.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);
    @Mapping(source = "id", target = "userId")
    @Mapping(source = "password", target = "password", qualifiedByName = "hidePassword")
    RegisterDTO userToRegisterDTO(User user);

    @Mapping(source = "id", target = "userId")
    @Mapping(source = "password", target = "password", qualifiedByName = "hidePassword")
    List<RegisterDTO> usersToRegisterDTOs(List<User> users);

    @Named("hidePassword")
    static String hidePassword(String password) {
        return password.replace(password, "*****");
    }
}
