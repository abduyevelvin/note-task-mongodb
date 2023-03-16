package com.task.note.auth.controller;

import com.task.note.auth.dto.LoginDTO;
import com.task.note.auth.service.AuthService;
import com.task.note.dto.RegisterDTO;
import com.task.note.dto.ResponseDTO;
import com.task.note.mapper.UserMapper;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@Validated
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final UserMapper userMapper;

    @PostMapping("/register")
    @ApiOperation(value = "returns registered/created user", notes = "need to provide valid user in body for creation")
    public ResponseEntity<ResponseDTO<Object>> register(@Valid @RequestBody RegisterDTO registerDTO){
        var user = userMapper.userToRegisterDTO(authService.registerUser(registerDTO));
        var responseDTO = ResponseDTO.builder().responseMsg("User registered...").data(user).build();

        return new ResponseEntity<>(responseDTO, CREATED);
    }

    @PostMapping("/login")
    @ApiOperation(value = "returns token for success login", notes = "need to provide valid login in body")
    public ResponseEntity<ResponseDTO<Object>> login(@Valid @RequestBody LoginDTO loginDTO){
        var token = authService.login(loginDTO);
        var responseDTO = ResponseDTO.builder().responseMsg("Login successful.").data(token).build();

        return new ResponseEntity<>(responseDTO, OK);
    }
}
