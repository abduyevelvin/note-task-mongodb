package com.task.note.service.impl;

import com.task.note.dto.EditUserDTO;
import com.task.note.enums.APIErrorCode;
import com.task.note.exception.UserNotFoundException;
import com.task.note.model.User;
import com.task.note.repository.NoteRepository;
import com.task.note.repository.UserRepository;
import com.task.note.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final NoteRepository noteRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public List<User> getUsers() {
        log.info("Fetching all users from DB...");
        return userRepository.findAll();
    }

    @Override
    public User getUser(String username) {
        log.info(String.format("Fetching user by username: %s", username));
        return userRepository.findByUsername(username)
                .orElseThrow(() -> {
                    log.error(String.format("User not found with username: %s", username));
                    return new UserNotFoundException(APIErrorCode.USER_NOT_FOUND);
                });
    }

    @Override
    public User editUser(String username, EditUserDTO userDTO) {
        var user = userRepository.findByUsername(username)
                .orElseThrow(() -> {
                    log.error(String.format("User not found with username: %s", username));
                    return new UserNotFoundException(APIErrorCode.USER_NOT_FOUND);
                });

        if (userDTO.getPassword() != null && !userDTO.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        }
        if (userDTO.getRoles() != null && !userDTO.getRoles().isEmpty()) {
            user.getRoles().addAll(userDTO.getRoles());
        }

        log.info(String.format("Updating user with username: %s", username));
        return userRepository.save(user);
    }

    @Override
    public User deleteUser(String username) {
        var user = userRepository.findByUsername(username)
                .orElseThrow(() -> {
                    log.error(String.format("User not found with username: %s", username));
                    return new UserNotFoundException(APIErrorCode.USER_NOT_FOUND);
                });

        log.info(String.format("Fetching notes by username: %s", username));
        var notes = noteRepository.findAllByNoteCreator(username);

        log.info(String.format("Assigning creator 'anonymousUser' to the notes created by user: %s", username));
        notes.forEach(note -> note.setNoteCreator("anonymousUser"));

        log.info(String.format("Removing likes from notes which created by user: %s", username));
        notes.stream().filter(note -> note.getLikedUsers().contains(username))
                .forEach(note -> note.getLikedUsers().remove(username));

        log.info("Saving notes to the DB...");
        noteRepository.saveAll(notes);

        log.info(String.format("Deleting user with username: %s", username));
        userRepository.delete(user);

        return user;
    }
}
