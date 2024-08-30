package com.eastwoo.springdockerec2.board.service;

import com.eastwoo.springdockerec2.board.dto.UserDto;
import com.eastwoo.springdockerec2.board.dto.UserRegistrationDto;
import com.eastwoo.springdockerec2.board.entity.User;
import com.eastwoo.springdockerec2.board.exception.DuplicateUserException;
import com.eastwoo.springdockerec2.board.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.eastwoo.springdockerec2.board.exception.ResourceNotFoundException;

import java.util.Optional;

/**
 * packageName    : com.eastwoo.springdockerec2.board.service
 * fileName       : UserService
 * author         : dongwoo
 * date           : 2024-08-22
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-08-22        dongwoo       최초 생성
 */
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

/*    public Optional<UserDto> findUserById(Long id) {
        return userRepository.findById(id).map(this::convertToDto);
    }

    public Optional<UserDto> findUserByUsername(String username) {
        return userRepository.findByUsername(username).map(this::convertToDto);
    }

    public Optional<UserDto> findUserByEmail(String email) {
        return userRepository.findByEmail(email).map(this::convertToDto);
    }*/

    public UserDto createUser(UserRegistrationDto registrationDto) {
        if (userRepository.existsByUserId(registrationDto.getUserId())) {
            throw new DuplicateUserException("이미 존재하는 사용자 ID입니다: " + registrationDto.getUserId());
        }

        if (userRepository.existsByEmail(registrationDto.getEmail())) {
            throw new DuplicateUserException("이미 존재하는 이메일입니다: " + registrationDto.getEmail());
        }

        String hashedPassword = passwordEncoder.encode(registrationDto.getPassword());

        User user = User.builder()
                .userId(registrationDto.getUserId())
                .username(registrationDto.getUsername())
                .email(registrationDto.getEmail())
                .password(hashedPassword)
                .build();

        User savedUser = userRepository.save(user);

        return convertToDto(savedUser);
    }

    private UserDto convertToDto(User user) {
        return UserDto.builder()
                .userId(user.getUserId())
                .username(user.getUsername())
                .email(user.getEmail())
                .build();
    }

    public boolean authenticateUser(String userId, String rawPassword) {
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("사용자 이름을 가진 사용자를 찾을 수 없습니다. 사용자 이름 : " + userId));

        return passwordEncoder.matches(rawPassword, user.getPassword());
    }

    private User convertToEntity(UserDto userDto) {
        return User.builder()
                .id(userDto.getId())
                .username(userDto.getUsername())
                .email(userDto.getEmail())
                .password(null)
                .build();
    }
}