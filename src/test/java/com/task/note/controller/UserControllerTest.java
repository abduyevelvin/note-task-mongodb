package com.task.note.controller;

import com.task.note.auth.service.impl.UserDetailsImpl;
import com.task.note.dto.EditUserDTO;
import com.task.note.mapper.UserMapper;
import com.task.note.model.User;
import com.task.note.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.Set;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static java.util.Collections.emptySet;
import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class UserControllerTest {
    private static final String USER_URL = "http://localhost:8080/users";
    private static final String USER_ID = "USER-ID";
    private static final String USERNAME = "USERNAME";
    private static final String PASSWORD = "PASSWORD";
    private static final Set<String> ROLES = Set.of("USER");

    @MockBean
    private UserService userService;

    private UserMapper userMapper;

    @Test
    void getAllUsersTest() {
        //given
        var user = createUser();
        var users = List.of(user);
        when(userService.getUsers()).thenReturn(users);

        //when
        var response = given()
                .when()
                .get(USER_URL + "/").then();

        //then
        response
                .assertThat()
                .statusCode(SC_OK)
                .body("responseMsg", is("Users list..."))
                .body("data[0].userId", is(USER_ID))
                .body("data[0].username", is(USERNAME))
                .body("data[0].password", is("*****"))
                .body("data[0].roles", is(notNullValue(String.class)))
                .body("data[0].likedNotes", is(empty()));
    }

    @Test
    void getUserTest() {
        //given
        var user = createUser();
        when(userService.getUser(USERNAME)).thenReturn(user);

        //when
        var response = given()
                .when()
                .get(USER_URL + "/" + USERNAME).then();

        //then
        response
                .assertThat()
                .statusCode(SC_OK)
                .body("responseMsg", is("User details..."))
                .body("data.userId", is(USER_ID))
                .body("data.username", is(USERNAME))
                .body("data.password", is("*****"))
                .body("data.roles", is(notNullValue(String.class)))
                .body("data.likedNotes", is(empty()));
    }

    @Test
    void getUserTestThrowExceptionWithoutLogin() {
        //given
        var user = new EditUserDTO();
        user.setPassword("new pass");

        //when
        var response = given()
                .when()
                .contentType(JSON)
                .accept(JSON)
                .body(user)
                .put(USER_URL + "/" + USERNAME).then();

        //then
        response
                .assertThat()
                .statusCode(SC_UNAUTHORIZED);
    }

    private User createUser() {
        return User.builder()
                .id(USER_ID)
                .username(USERNAME)
                .password(PASSWORD)
                .roles(ROLES)
                .likedNotes(emptySet())
                .build();
    }

    private void mockAuthentication() {
        var auth = mock(Authentication.class);

        when(auth.getPrincipal()).thenReturn(UserDetailsImpl.build(createUser()));

        var securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(auth);
        SecurityContextHolder.setContext(securityContext);
    }
}
