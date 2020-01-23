package com.example.library.web.controller;

import com.example.library.core.exception.UserNotFoundException;
import com.example.library.core.service.UserService;
import com.example.library.model.Role;
import com.example.library.model.User;
import com.example.library.web.config.security.jwt.JwtTokenProvider;
import com.example.library.web.converter.ApiConverter;
import com.example.library.web.converter.UserConverter;
import com.example.library.web.dto.ApiResponseDto;
import com.example.library.web.dto.UserDto;
import com.example.library.web.dto.enums.ApiError;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Authentication controller supports login and logout operations for users.
 */
@Slf4j
@RestController
@RequestMapping(value = "/auth")
public class AuthenticationController {

    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public AuthenticationController(UserService userService, JwtTokenProvider jwtTokenProvider) {
        this.userService = userService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    /**
     * Processes http post request to login a new user.
     *
     * @param username User username for login.
     * @param password User password for login.
     * @return ResponseEntity containing ApiResponse with logged user.
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity<ApiResponseDto<UserDto>> login(@RequestParam(value = "username") String username, @RequestParam(value = "password") String password) {
        User user;
        try {
            user = this.userService.getUserByUsername(username);
        } catch (UserNotFoundException e) {
            log.error("User with username: {} not found!", username, e);
            return ApiConverter.resolveApiError(ApiError.USER_DOES_NOT_EXIST);
        }

        if (Arrays.equals(user.getPasswordHash(), UserConverter.getPasswordHash(password))) {
            Set<Role> roles = user.getRoles();
            String token = this.jwtTokenProvider.createToken(username, roles.stream()
                    .map(Role::getName)
                    .collect(Collectors.toList()));

            UserDto userDto = UserConverter.db2dtoForLogin(user, token);

            ApiResponseDto<UserDto> response = ApiResponseDto.<UserDto>builder().content(userDto).build();
            return ResponseEntity.ok(response);
        } else {
            return ApiConverter.resolveApiError(ApiError.USER_INCORRECT_PASSWORD);
        }
    }

    /**
     * Processes http post request to logout the user.
     *
     * @param username User username for logout.
     * @return ResponseEntity containing ApiResponse with user after logout.
     */
    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    public ResponseEntity<ApiResponseDto<UserDto>> logout(@RequestParam(value = "username") String username) {
        User user;
        try {
            user = this.userService.getUserByUsername(username);
        } catch (UserNotFoundException e) {
            log.error("User with username: {} not found!", username, e);
            return ApiConverter.resolveApiError(ApiError.USER_DOES_NOT_EXIST);
        }

        user.setToken(null);
        try {
            this.userService.updateUser(user);
        } catch (UserNotFoundException e) {
            log.error("User with username: {} not found!", username, e);
            return ApiConverter.resolveApiError(ApiError.USER_DOES_NOT_EXIST);
        }
        return ResponseEntity.ok(ApiResponseDto.<UserDto>builder().build());
    }
}
