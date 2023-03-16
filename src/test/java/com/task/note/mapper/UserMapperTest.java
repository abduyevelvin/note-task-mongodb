package com.task.note.mapper;

import com.task.note.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Set;

import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UserMapperTest {
    @Autowired
    private UserMapper userMapper;

    @Test
    void userToRegisterDTOTest() {
        //given
        var user = User.builder()
                .id("test-id")
                .username("test-username")
                .password("test-password")
                .roles(Set.of("user"))
                .likedNotes(Set.of("note-id-1"))
                .build();

        //when
        var registerDTO = userMapper.userToRegisterDTO(user);

        //then
        assertSoftly(as -> {
            assertNotNull(registerDTO);
            assertEquals(user.getId(), registerDTO.getUserId());
            assertEquals(user.getUsername(), registerDTO.getUsername());
            assertEquals("*****", registerDTO.getPassword());
            assertEquals(user.getRoles().size(), registerDTO.getRoles().size());
            assertTrue(user.getRoles().containsAll(registerDTO.getRoles()));
            assertEquals(user.getLikedNotes().size(), registerDTO.getLikedNotes().size());
            assertTrue(user.getLikedNotes().containsAll(registerDTO.getLikedNotes()));
        });
    }
}
