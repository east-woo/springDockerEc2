package com.eastwoo.springdockerec2.board.service;

import com.eastwoo.springdockerec2.board.dto.UserDto;
import com.eastwoo.springdockerec2.board.dto.UserRegistrationDto;
import com.eastwoo.springdockerec2.board.entity.User;
import com.eastwoo.springdockerec2.board.exception.DuplicateUserException;
import com.eastwoo.springdockerec2.board.exception.ResourceNotFoundException;
import com.eastwoo.springdockerec2.board.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Date;
import java.util.Optional;


import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * packageName    : com.eastwoo.springdockerec2.board.service
 * fileName       : UserServiceTest
 * author         : dongwoo
 * date           : 2024-08-26
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-08-26        dongwoo       최초 생성
 */
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void 사용자_등록_성공() {
        UserRegistrationDto registrationDto = new UserRegistrationDto("user123", "username", "user@example.com", "password");
        User user = User.builder()
                .userId("user123")
                .username("username")
                .email("user@example.com")
                .password("hashedPassword")
                .build();


        when(userRepository.existsByUserId(registrationDto.getUserId())).thenReturn(false);
        when(userRepository.existsByEmail(registrationDto.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(registrationDto.getPassword())).thenReturn("hashedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);

        UserDto userDto = userService.createUser(registrationDto);

        assertNotNull(userDto);
        assertEquals("user123", userDto.getUserId());
        assertEquals("username", userDto.getUsername());
        assertEquals("user@example.com", userDto.getEmail());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    public void 사용자_ID_중복_테스트() {
        UserRegistrationDto registrationDto = new UserRegistrationDto("user123", "username", "user@example.com", "password");

        when(userRepository.existsByUserId(registrationDto.getUserId())).thenReturn(true);

        DuplicateUserException thrown = assertThrows(DuplicateUserException.class, () -> userService.createUser(registrationDto));
        assertEquals("이미 존재하는 사용자 ID입니다: user123", thrown.getMessage());
    }

    @Test
    public void 이메일_중복_테스트() {
        UserRegistrationDto registrationDto = new UserRegistrationDto("user123", "username", "user@example.com", "password");

        when(userRepository.existsByEmail(registrationDto.getEmail())).thenReturn(true);

        DuplicateUserException thrown = assertThrows(DuplicateUserException.class, () -> userService.createUser(registrationDto));
        assertEquals("이미 존재하는 이메일입니다: user@example.com", thrown.getMessage());
    }

    @Test
    public void 사용자_인증_성공_테스트() {
        String userId = "username";
        String rawPassword = "password";
        User user = User.builder()
                .password("hashedPassword")
                .build();

        when(userRepository.findByUserId(userId)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(rawPassword, user.getPassword())).thenReturn(true);

        boolean result = userService.authenticateUser(userId, rawPassword);

        assertTrue(result);
    }

    @Test
    public void 사용자_인증_실패_테스트() {
        String username = "username";
        String rawPassword = "password";

        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        ResourceNotFoundException thrown = assertThrows(ResourceNotFoundException.class, () -> userService.authenticateUser(username, rawPassword));
        assertEquals("사용자 이름을 가진 사용자를 찾을 수 없습니다. 사용자 이름 : username", thrown.getMessage());
    }
}
