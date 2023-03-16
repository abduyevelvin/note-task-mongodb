package com.task.note.controller;

import com.task.note.dto.EditUserDTO;
import com.task.note.dto.ResponseDTO;
import com.task.note.mapper.UserMapper;
import com.task.note.service.UserService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.*;

@RestController
@Validated
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final UserMapper userMapper;

    @GetMapping("/")
    @ApiOperation("returns all users from DB")
    public ResponseEntity<ResponseDTO<Object>> getUsers(){
        var users = userService.getUsers();
        var responseDTO = ResponseDTO.builder().responseMsg("Users list...")
                .data(users.isEmpty() ? "No User in the DB" : userMapper.usersToRegisterDTOs(users)).build();

        return new ResponseEntity<>(responseDTO, OK);
    }
    @GetMapping("/{username}")
    @ApiOperation(value = "returns user by username or not found exception",
            notes = "need to provide existing user name")
    public ResponseEntity<ResponseDTO<Object>> getUser(@PathVariable("username") String username){
        var user = userService.getUser(username);
        var responseDTO = ResponseDTO.builder().responseMsg("User details...")
                .data(userMapper.userToRegisterDTO(user)).build();

        return new ResponseEntity<>(responseDTO, OK);
    }

    @PutMapping("/{username}")
    @PreAuthorize("hasAuthority('USER')")
    @ApiOperation(value = "returns updated user or not found exception",
            notes = "need to provide valid user in body and existing username to update an user")
    public ResponseEntity<ResponseDTO<Object>> editUser(@PathVariable("username") String username, @RequestBody EditUserDTO userDTO){
        var user = userService.editUser(username, userDTO);
        var responseDTO = ResponseDTO.builder().responseMsg("Edited user...")
                .data(userMapper.userToRegisterDTO(user)).build();

        return new ResponseEntity<>(responseDTO, ACCEPTED);
    }

    @DeleteMapping("/{username}")
    @PreAuthorize("hasAuthority('USER')")
    @ApiOperation(value = "returns a deleted user or not found exception",
            notes = "need to provide existing username")
    public ResponseEntity<ResponseDTO<Object>> deleteUser(@PathVariable("username") String username){
        var user = userService.deleteUser(username);
        var responseDTO = ResponseDTO.builder().responseMsg("Deleted user...")
                .data(userMapper.userToRegisterDTO(user)).build();

        return new ResponseEntity<>(responseDTO, NO_CONTENT);
    }
}
