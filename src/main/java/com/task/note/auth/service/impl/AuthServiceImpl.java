package com.task.note.auth.service.impl;

import com.task.note.auth.dto.LoginDTO;
import com.task.note.auth.exception.InvalidCredentialsException;
import com.task.note.auth.jwt.JwtUtils;
import com.task.note.auth.service.AuthService;
import com.task.note.dto.RegisterDTO;
import com.task.note.enums.APIErrorCode;
import com.task.note.exception.UserExistsException;
import com.task.note.exception.UserNotFoundException;
import com.task.note.model.User;
import com.task.note.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static java.util.Collections.emptySet;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;

    @Override
    public User registerUser(RegisterDTO registerDTO) {
        if (userRepository.existsByUsername(registerDTO.getUsername())) {
            log.error(String.format("User already exists with username: %s", registerDTO.getUsername()));
            throw new UserExistsException(APIErrorCode.USER_EXISTS);
        }

        var user = User.builder()
                .username(registerDTO.getUsername())
                .password(passwordEncoder.encode(registerDTO.getPassword()))
                .roles(registerDTO.getRoles())
                .likedNotes(emptySet())
                .build();

        log.info("Saving user to the DB...");
        return userRepository.save(user);
    }

    @Override
    public String login(LoginDTO loginDTO) {
        var userEntity = userRepository.findByUsername(loginDTO.getUsername())
                .orElseThrow(() -> new UserNotFoundException(APIErrorCode.USER_NOT_FOUND));

        if (passwordEncoder.matches(loginDTO.getPassword(), userEntity.getPassword())) {
            var authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginDTO.getUsername(), loginDTO.getPassword()));
            return jwtUtils.generateJwtToken(authentication);
        } else {
            log.error("Invalid credentials provided...");
            throw new InvalidCredentialsException(APIErrorCode.INVALID_CREDENTIALS);
        }
    }
}
