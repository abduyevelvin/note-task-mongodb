package com.task.note.service;

import com.task.note.dto.EditUserDTO;
import com.task.note.model.User;

import java.util.List;

public interface UserService {
    List<User> getUsers();
    User getUser(String username);
    User editUser(String username, EditUserDTO userDTO);
    User deleteUser(String username);
}
