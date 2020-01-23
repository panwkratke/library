package com.example.library.web.controller;

import com.example.library.core.exception.ActiveBorrowOrdersException;
import com.example.library.core.exception.RoleNotFoundException;
import com.example.library.core.exception.UserExistException;
import com.example.library.core.exception.UserNotFoundException;
import com.example.library.core.service.RoleService;
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
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * User controller supports all controller operations regarding http requests for users. (REST)
 */
@Slf4j
@RestController
@RequestMapping(value = "/users")
public class UserController {

    private final UserService userService;
    private final RoleService roleService;
    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public UserController(UserService userService, RoleService roleService, JwtTokenProvider jwtTokenProvider) {
        this.userService = userService;
        this.roleService = roleService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    /**
     * Processes http post request to save a new user to the repository.
     *
     * @param dto User dto to be processed.
     * @return ResponseEntity containing ApiResponse with saved user.
     */
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ResponseEntity<ApiResponseDto<UserDto>> registerUser(@RequestBody UserDto dto) {
        Role role;
        try {
            role = this.roleService.getRoleByRoleName(dto.getRole());
        } catch (RoleNotFoundException e) {
            log.error("Role with role name: {} doesn't exists!", dto.getRole(), e);
            return ApiConverter.resolveApiError(ApiError.ROLE_DOES_NOT_EXIST);
        }

        User user = UserConverter.dto2db(dto, role);

        try {
            this.userService.registerUser(user);
        } catch (UserExistException e) {
            log.error("User with username: {} already exists!", user.getUsername(), e);
            return ApiConverter.resolveApiError(ApiError.DUPLICATED_USER);
        }
        return UserConverter.db2re(user);
    }

    /**
     * Processes http get request to retrieve a user by user id from the repository.
     *
     * @param id User id to be searched.
     * @return ResponseEntity containing ApiResponse with retrieved user.
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<ApiResponseDto<UserDto>> getUserById(@PathVariable Long id) {
        User user;
        try {
            user = this.userService.getUserById(id);
        } catch (UserNotFoundException e) {
            log.error("User with id: {} not found", id, e);
            return ApiConverter.resolveApiError(ApiError.USER_DOES_NOT_EXIST);
        }
        return UserConverter.db2re(user);
    }

    /**
     * Processes http get request to retrieve a user by user username from the repository.
     *
     * @param username User username to be searched.
     * @return ResponseEntity containing ApiResponse with retrieved user.
     */
    @RequestMapping(value = "/username/{username}", method = RequestMethod.GET)
    public ResponseEntity<ApiResponseDto<UserDto>> getUserByUsername(@PathVariable String username) {
        User user;
        try {
            user = this.userService.getUserByUsername(username);
        } catch (UserNotFoundException e) {
            log.error("User with username: {} not found", username, e);
            return ApiConverter.resolveApiError(ApiError.USER_DOES_NOT_EXIST);
        }
        return UserConverter.db2re(user);
    }

    /**
     * Processes http get request to retrieve a list of all users from the repository.
     *
     * @return The list of all retrieved users.
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public List<UserDto> getUsers() {
        return UserConverter.userDbListToUserDtoList(this.userService.getUsers());
    }

    /**
     * Processes http delete request to delete a user by user id from the repository.
     *
     * @param id User id to be searched.
     * @return ResponseEntity containing ApiResponse without a body.
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<ApiResponseDto<Void>> deleteUserById(@PathVariable Long id) {
        try {
            this.userService.deleteUserById(id);
        } catch (UserNotFoundException e) {
            log.error("User with id: {} not found", id, e);
            return ApiConverter.resolveApiError(ApiError.USER_DOES_NOT_EXIST);
        } catch (ActiveBorrowOrdersException a) {
            log.error("User with id: {} has active borrow orders", id, a);
            return ApiConverter.resolveApiError(ApiError.ACTIVE_BORROW_ORDERS);
        }
        return ResponseEntity.ok().body(ApiResponseDto.<Void>builder().build());
    }

    /**
     * Processes http put request to update a user in the repository.
     *
     * @param dto User dto to be processed.
     * @return ResponseEntity containing ApiResponse with updated user.
     */
    @RequestMapping(method = RequestMethod.PUT)
    public ResponseEntity<ApiResponseDto<UserDto>> updateUser(@RequestBody UserDto dto) {
        User user = new User();
        Set<Role> roles;
        try {
            user = this.userService.getUserById(dto.getId());
            roles = user.getRoles();
            UserConverter.dto2dbForUpdate(user, dto);
            String token = this.jwtTokenProvider.createToken(user.getUsername(), roles.stream()
                    .map(Role::getName)
                    .collect(Collectors.toList()));
            user.setToken(token);
            this.userService.updateUser(user);
        } catch (UserNotFoundException e) {
            log.error("User with id: {} not found", user.getId(), e);
            return ApiConverter.resolveApiError(ApiError.USER_DOES_NOT_EXIST);
        }
        return UserConverter.db2reForUpdate(user);
    }
}
