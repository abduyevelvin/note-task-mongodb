package com.task.note.auth.service;

import com.task.note.auth.dto.LoginDTO;
import com.task.note.dto.RegisterDTO;
import com.task.note.model.User;

public interface AuthService {
    User registerUser(RegisterDTO registerDTO);
    String login(LoginDTO loginDTO);
}
