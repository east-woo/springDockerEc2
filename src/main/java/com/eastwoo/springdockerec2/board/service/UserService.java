package com.eastwoo.springdockerec2.board.service;

import com.eastwoo.springdockerec2.board.dto.UserDto;
import com.eastwoo.springdockerec2.board.dto.UserRegistrationDto;
import com.eastwoo.springdockerec2.board.entity.User;
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

    public Optional<UserDto> findUserById(Long id) {
        return userRepository.findById(id).map(this::convertToDto);
    }

    public Optional<UserDto> findUserByUsername(String username) {
        return userRepository.findByUsername(username).map(this::convertToDto);
    }

    public Optional<UserDto> findUserByEmail(String email) {
        return userRepository.findByEmail(email).map(this::convertToDto);
    }

    public UserDto createUser(UserRegistrationDto registrationDto) {
        // Hash the password
        String hashedPassword = passwordEncoder.encode(registrationDto.getPassword());

        // Create a new User entity using the builder pattern
        User user = User.builder()
                .username(registrationDto.getUsername())
                .email(registrationDto.getEmail())
                .password(hashedPassword)
                .build();

        // Save the user entity
        User savedUser = userRepository.save(user);

        // Convert the saved user back to a DTO (excluding password)
        return convertToDto(savedUser);
    }
    private UserDto convertToDto(User user) {
        return new UserDto(user.getId(), user.getUsername(), user.getEmail());
    }

    public boolean authenticateUser(String username, String rawPassword) {
        // Find the user by username
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with username " + username));

        // Compare the raw password with the encoded password stored in the database
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