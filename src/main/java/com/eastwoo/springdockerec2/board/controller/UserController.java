package com.eastwoo.springdockerec2.board.controller;

import com.eastwoo.springdockerec2.board.config.JwtTokenProvider;
import com.eastwoo.springdockerec2.board.dto.UserDto;
import com.eastwoo.springdockerec2.board.dto.UserRegistrationDto;
import com.eastwoo.springdockerec2.board.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * packageName    : com.eastwoo.springdockerec2.board.controller
 * fileName       : UserController
 * author         : dongwoo
 * date           : 2024-08-22
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-08-22        dongwoo       최초 생성
 */
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;

    public UserController(UserService userService, JwtTokenProvider jwtTokenProvider) {
        this.userService = userService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @PostMapping("/register")
    public ResponseEntity<UserDto> registerUser(@Valid @RequestBody UserRegistrationDto registrationDto) {
        UserDto userDto = userService.createUser(registrationDto);
        return new ResponseEntity<>(userDto, HttpStatus.CREATED);
    }

    @GetMapping("/getUserName")
    public ResponseEntity<UserDto> getUserName(@RequestHeader("Authorization") String authorizationHeader) {
        String token = authorizationHeader.substring(7);
        String userId = jwtTokenProvider.getUserIdFromToken(token);
        return userService.findUserNameByUserId(userId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestBody UserRegistrationDto registrationDto) {

        boolean isAuthenticated = userService.authenticateUser(registrationDto.getUserId(), registrationDto.getPassword());
        if (isAuthenticated) {
            String token = jwtTokenProvider.createToken(registrationDto.getUserId());
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.TEXT_PLAIN_VALUE)
                    .body(token);
        } else {
            return ResponseEntity.status(401).body("잘못된 자격 증명");
        }
    }
}