package com.example.library.web.converter;

import com.example.library.model.Book;
import com.example.library.model.Role;
import com.example.library.model.User;
import com.example.library.web.dto.ApiResponseDto;
import com.example.library.web.dto.UserDto;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.http.ResponseEntity;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * User converter supports all required operations regarding conversions between user objects.
 */
public class UserConverter {

    /**
     * Converts user dto to user db.
     *
     * @param dto  User dto to be converted.
     * @param role Role to be included in user db.
     * @return User after conversion.
     */
    public static User dto2db(UserDto dto, Role role) {
        User db = new User();
        UserConverter.dto2db(db, dto);
        db.getRoles().add(role);
        return db;
    }

    private static void dto2db(User db, UserDto dto) {
        db.setId(dto.getId());
        db.setFirstName(dto.getFirstName());
        db.setLastName(dto.getLastName());
        db.setUsername(dto.getUsername());
        db.setPasswordHash(getPasswordHash(dto.getPassword()));
        Set<Book> books = new HashSet<>();
        db.setBorrowedBooks(books);
        Set<Role> roles = new HashSet<>();
        db.setRoles(roles);
    }

    /**
     * Encodes user password.
     *
     * @param password User password to be encoded.
     * @return Encoded user password.
     */
    public static byte[] getPasswordHash(String password) {
        return DigestUtils.sha256(password.getBytes(StandardCharsets.UTF_8));
    }

    private static String roleSetToString(User db) {
        String role = "";
        for (Role r : db.getRoles()) {
            role = r.getName();
            break;
        }
        return role;
    }

    /**
     * Converts user db to user dto for login.
     *
     * @param db    User db to be converted.
     * @param token Token to be included in user dto.
     * @return UserDto after conversion.
     */
    public static UserDto db2dtoForLogin(User db, String token) {
        UserDto dto = new UserDto();
        dto.setUsername(db.getUsername());
        dto.setToken(token);
        dto.setRole(UserConverter.roleSetToString(db));
        return dto;
    }

    /**
     * Converts user db to user dto.
     *
     * @param db User db to be converted.
     * @return UserDto after conversion.
     */
    public static UserDto db2dto(User db) {
        UserDto dto = new UserDto();
        UserConverter.db2dto(dto, db);
        return dto;
    }

    private static void db2dto(UserDto dto, User db) {
        dto.setId(db.getId());
        dto.setFirstName(db.getFirstName());
        dto.setLastName(db.getLastName());
        dto.setUsername(db.getUsername());
        dto.setRole(UserConverter.roleSetToString(db));
        dto.setBorrowedBooks(db.getBorrowedBooks());
        dto.setCreationDate(db.getCreationDate().getTime().toString());
    }

    /**
     * Converts a list of users db to list of users dto.
     *
     * @param dbUserList List of users db to be converted.
     * @return List of users dto after conversion.
     */
    public static List<UserDto> userDbListToUserDtoList(List<User> dbUserList) {
        List<UserDto> userDtoList = new ArrayList<>();
        for (User u : dbUserList) {
            userDtoList.add(db2dto(u));
        }
        return userDtoList;
    }

    /**
     * Converts user db to ResponseEntity.
     *
     * @param db User db to be converted.
     * @return ResponseEntity containing ApiResponse with converted User to UserDto.
     */
    public static ResponseEntity<ApiResponseDto<UserDto>> db2re(User db) {
        UserDto dto = UserConverter.db2dto(db);
        return ResponseEntity.ok().body(ApiResponseDto.<UserDto>builder().content(dto).build());
    }

    /**
     * Converts user db to ResponseEntity for the user update.
     *
     * @param db User db to be converted.
     * @return ResponseEntity containing ApiResponse with converted User to UserDto with the new token.
     */
    public static ResponseEntity<ApiResponseDto<UserDto>> db2reForUpdate(User db) {
        ResponseEntity<ApiResponseDto<UserDto>> re = db2re(db);
        re.getBody().getContent().setToken(db.getToken());
        return re;
    }

    /**
     * Converts user dto to user db for the user update.
     *
     * @param db  User db to be prepared for the update.
     * @param dto UserDto containing a new data for the user update.
     */
    public static void dto2dbForUpdate(User db, UserDto dto) {
        db.setFirstName(dto.getFirstName());
        db.setLastName(dto.getLastName());
        db.setUsername(dto.getUsername());
        db.setPasswordHash(getPasswordHash(dto.getPassword()));
    }
}
