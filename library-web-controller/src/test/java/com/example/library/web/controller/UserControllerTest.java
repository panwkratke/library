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
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.mockito.Mockito.*;

@Slf4j
@RunWith(PowerMockRunner.class)
@PrepareForTest({UserConverter.class, ApiConverter.class})
public class UserControllerTest {

    private Long mockedId;
    private String mockedUsername;
    private String mockedRoleName;
    private User mockedUser;
    private UserDto mockedUserDto;
    private Role mockedRole;
    private UserService userServiceMock;
    private RoleService roleServiceMock;
    private JwtTokenProvider jwtTokenProviderMock;
    private UserController userController;
    private ResponseEntity<ApiResponseDto<UserDto>> actual;
    private ResponseEntity<ApiResponseDto<Void>> voidActual;
    private List<User> mockedUserDbTable;
    private List<UserDto> actualUserDbTable;

    @Before
    public void initialize() {
        mockedId = 1L;
        mockedUsername = "mockedUsername";
        mockedRoleName = "mockedRoleName";
        mockedUser = new User();
        mockedUserDto = new UserDto();
        mockedUserDto.setId(mockedId);
        mockedUserDto.setRole(mockedRoleName);
        mockedRole = new Role();
        mockedRole.setName(mockedRoleName);
        Set<Role> mockedRoleSet = new HashSet<>();
        mockedRoleSet.add(mockedRole);
        mockedUser.setId(mockedId);
        mockedUser.setRoles(mockedRoleSet);
        userServiceMock = mock(UserService.class);
        roleServiceMock = mock(RoleService.class);
        jwtTokenProviderMock = mock(JwtTokenProvider.class);
        userController = new UserController(userServiceMock, roleServiceMock, jwtTokenProviderMock);
        mockedUserDbTable = new ArrayList<>();
        mockedUserDbTable.add(mockedUser);
        actualUserDbTable = new ArrayList<>();
        List<UserDto> mockedUserDtoTable = new ArrayList<>();
        mockedUserDtoTable.add(mockedUserDto);
        ResponseEntity<ApiResponseDto<UserDto>> mockedResponseEntity = new ResponseEntity<>(HttpStatus.OK);
        PowerMockito.mockStatic(UserConverter.class);
        BDDMockito.given(UserConverter.dto2db(any(UserDto.class), any(Role.class))).willReturn(mockedUser);
        BDDMockito.given(UserConverter.db2dto(any(User.class))).willReturn(mockedUserDto);
        BDDMockito.given(UserConverter.db2re(nullable(User.class))).willReturn(mockedResponseEntity);
        BDDMockito.given(UserConverter.db2reForUpdate(any(User.class))).willReturn(mockedResponseEntity);
        BDDMockito.given(UserConverter.userDbListToUserDtoList(mockedUserDbTable)).willReturn(mockedUserDtoTable);
    }

    @Test
    public void registerUser_when_user_does_not_exists() throws UserExistException, RoleNotFoundException {
        //given
        doNothing().when(userServiceMock).registerUser(any(User.class));
        when(roleServiceMock.getRoleByRoleName(mockedRoleName)).thenReturn(mockedRole);

        //when
        actual = userController.registerUser(mockedUserDto);

        //then
        Assert.assertEquals("not OK request", HttpStatus.OK, actual.getStatusCode());
    }

    @Test
    public void registerUser_when_user_exist() throws UserExistException, RoleNotFoundException {
        //given
        doThrow(UserExistException.createWithUsername("")).when(userServiceMock).registerUser(any(User.class));
        when(roleServiceMock.getRoleByRoleName(mockedRoleName)).thenReturn(mockedRole);

        //when
        actual = userController.registerUser(mockedUserDto);

        //then
        Assert.assertEquals("not BAD_REQUEST", HttpStatus.BAD_REQUEST, actual.getStatusCode());
        Assert.assertNotNull("body is null", actual.getBody());

        //checking if ApiError.DUPLICATED_USER is returned in actual.getBody()
        Assert.assertEquals("wrong api error", Integer.valueOf(ApiError.DUPLICATED_USER.getApiError()), actual.getBody().getApiError());
        Assert.assertEquals("wrong http status", Integer.valueOf(ApiError.DUPLICATED_USER.getHttpStatus().value()), actual.getBody().getHttpError());
        Assert.assertEquals("wrong api error msg", ApiError.DUPLICATED_USER.getApiErrorMsg(), actual.getBody().getApiErrorMsg());
    }

    @Test
    public void getUserById_when_user_exist() throws UserNotFoundException {
        //given
        when(userServiceMock.getUserById(mockedId)).thenReturn(mockedUser);

        //when
        actual = userController.getUserById(mockedId);

        //then
        Assert.assertEquals("Response not equal", HttpStatus.OK, actual.getStatusCode());
    }


    @Test
    public void getUserById_when_user_does_not_exist() throws UserNotFoundException {
        //given
        doThrow(UserNotFoundException.createWithId(mockedId)).when(userServiceMock).getUserById(mockedId);

        //when
        actual = userController.getUserById(mockedId);

        //then
        Assert.assertEquals("not BAD_REQUEST", HttpStatus.BAD_REQUEST, actual.getStatusCode());
        Assert.assertNotNull("body is null", actual.getBody());

        //checking if ApiError.USER_DOES_NOT_EXIST is returned in actual.getBody()
        Assert.assertEquals("wrong api error", Integer.valueOf(ApiError.USER_DOES_NOT_EXIST.getApiError()), actual.getBody().getApiError());
        Assert.assertEquals("wrong http status", Integer.valueOf(ApiError.USER_DOES_NOT_EXIST.getHttpStatus().value()), actual.getBody().getHttpError());
        Assert.assertEquals("wrong api error msg", ApiError.USER_DOES_NOT_EXIST.getApiErrorMsg(), actual.getBody().getApiErrorMsg());
    }

    @Test
    public void getUserByUsername_when_user_exist() throws UserNotFoundException {
        //given
        when(userServiceMock.getUserByUsername(mockedUsername)).thenReturn(mockedUser);

        //when
        actual = userController.getUserByUsername(mockedUsername);

        //then
        Assert.assertEquals("Response not equal", HttpStatus.OK, actual.getStatusCode());
    }

    @Test
    public void getUserByUsername_when_user_does_not_exist() throws UserNotFoundException {
        //given
        doThrow(UserNotFoundException.createWithUsername(mockedUsername)).when(userServiceMock).getUserByUsername(mockedUsername);

        //when
        actual = userController.getUserByUsername(mockedUsername);

        //then
        Assert.assertEquals("not BAD_REQUEST", HttpStatus.BAD_REQUEST, actual.getStatusCode());
        Assert.assertNotNull("body is null", actual.getBody());

        //checking if ApiError.USER_DOES_NOT_EXIST is returned in actual.getBody()
        Assert.assertEquals("wrong api error", Integer.valueOf(ApiError.USER_DOES_NOT_EXIST.getApiError()), actual.getBody().getApiError());
        Assert.assertEquals("wrong http status", Integer.valueOf(ApiError.USER_DOES_NOT_EXIST.getHttpStatus().value()), actual.getBody().getHttpError());
        Assert.assertEquals("wrong api error msg", ApiError.USER_DOES_NOT_EXIST.getApiErrorMsg(), actual.getBody().getApiErrorMsg());
    }

    @Test
    public void getUsers_with_correct_list() {
        //given
        when(userServiceMock.getUsers()).thenReturn(mockedUserDbTable);

        //when
        actualUserDbTable = userController.getUsers();

        //then
        Assert.assertEquals("Incorrect array size", mockedUserDbTable.size(), actualUserDbTable.size());

        //checking if elements in both lists are equal
        for (int i = 0; i < mockedUserDbTable.size(); i++) {
            User expectedUser = mockedUserDbTable.get(i);
            UserDto actualUser = actualUserDbTable.get(i);
            Assert.assertEquals(String.format("Element %s is not equal actual %s for i = %d", expectedUser.getFirstName(), actualUser.getFirstName(), i), expectedUser.getFirstName(), actualUser.getFirstName());
            Assert.assertEquals(String.format("Element %s is not equal actual %s for i = %d", expectedUser.getLastName(), actualUser.getLastName(), i), expectedUser.getLastName(), actualUser.getLastName());
            Assert.assertEquals(String.format("Element %s is not equal actual %s for i = %d", expectedUser.getUsername(), actualUser.getUsername(), i), expectedUser.getUsername(), actualUser.getUsername());
            Assert.assertEquals(String.format("Element %s is not equal actual %s for i = %d", expectedUser.getId(), actualUser.getId(), i), expectedUser.getId(), actualUser.getId());
        }
    }

    @Test
    public void getUsers_with_incorrect_list() {
        //given
        List<User> incorrectUserDbTable = new ArrayList<>();
        when(userServiceMock.getUsers()).thenReturn(incorrectUserDbTable);

        //when
        actualUserDbTable = userController.getUsers();

        //then
        Assert.assertNotEquals("Correct array size", mockedUserDbTable.size(), actualUserDbTable.size());
    }

    @Test
    public void updateUserById_when_user_exist() throws UserNotFoundException {
        //given
        doNothing().when(userServiceMock).updateUser(mockedUser);
        when(userServiceMock.getUserById(any(Long.class))).thenReturn(mockedUser);
        when(jwtTokenProviderMock.createToken(nullable(String.class), nullable(List.class))).thenReturn("mocked_jwt_token");

        //when
        actual = userController.updateUser(mockedUserDto);

        //then
        Assert.assertEquals("Response not equal", HttpStatus.OK, actual.getStatusCode());
    }

    @Test
    public void updateUserById_when_user_does_not_exist() throws UserNotFoundException {
        //given
        doThrow(UserNotFoundException.createWithId(mockedId)).when(userServiceMock).getUserById(mockedId);
        doThrow(UserNotFoundException.createWithId(mockedId)).when(userServiceMock).updateUser(mockedUser);
        when(jwtTokenProviderMock.createToken(nullable(String.class), nullable(List.class))).thenReturn("mocked_jwt_token");

        //when
        actual = userController.updateUser(mockedUserDto);

        //then
        Assert.assertEquals("not BAD_REQUEST", HttpStatus.BAD_REQUEST, actual.getStatusCode());
        Assert.assertNotNull("body is null", actual.getBody());

        //checking if ApiError.USER_DOES_NOT_EXIST is returned in actual.getBody()
        Assert.assertEquals("wrong api error", Integer.valueOf(ApiError.USER_DOES_NOT_EXIST.getApiError()), actual.getBody().getApiError());
        Assert.assertEquals("wrong http status", Integer.valueOf(ApiError.USER_DOES_NOT_EXIST.getHttpStatus().value()), actual.getBody().getHttpError());
        Assert.assertEquals("wrong api error msg", ApiError.USER_DOES_NOT_EXIST.getApiErrorMsg(), actual.getBody().getApiErrorMsg());
    }

    @Test
    public void deleteUserById_when_user_exist() throws UserNotFoundException, ActiveBorrowOrdersException {
        //given
        doNothing().when(userServiceMock).deleteUserById(any(Long.class));

        //when
        voidActual = userController.deleteUserById(mockedId);

        //when then
        Assert.assertEquals("not BAD_REQUEST", HttpStatus.OK, voidActual.getStatusCode());
        Assert.assertNotNull("body is null", voidActual.getBody());
    }

    @Test
    public void deleteUserById_when_user_does_not_exist() throws UserNotFoundException, ActiveBorrowOrdersException {
        //given
        doThrow(UserNotFoundException.createWithId(mockedId)).when(userServiceMock).deleteUserById(mockedId);

        //when
        voidActual = userController.deleteUserById(mockedId);

        //then
        Assert.assertEquals("not BAD_REQUEST", HttpStatus.BAD_REQUEST, voidActual.getStatusCode());
        Assert.assertNotNull("body is null", voidActual.getBody());

        //checking if ApiError.USER_DOES_NOT_EXIST is returned in actual.getBody()
        Assert.assertEquals("wrong api error", Integer.valueOf(ApiError.USER_DOES_NOT_EXIST.getApiError()), voidActual.getBody().getApiError());
        Assert.assertEquals("wrong http status", Integer.valueOf(ApiError.USER_DOES_NOT_EXIST.getHttpStatus().value()), voidActual.getBody().getHttpError());
        Assert.assertEquals("wrong api error msg", ApiError.USER_DOES_NOT_EXIST.getApiErrorMsg(), voidActual.getBody().getApiErrorMsg());
    }

    @Test
    public void deleteUserById_when_user_has_active_borrow_orders() throws UserNotFoundException, ActiveBorrowOrdersException {
        //given
        doThrow(ActiveBorrowOrdersException.createWithUserId(mockedId)).when(userServiceMock).deleteUserById(mockedId);

        //when
        voidActual = userController.deleteUserById(mockedId);

        //then
        Assert.assertEquals("not BAD_REQUEST", HttpStatus.BAD_REQUEST, voidActual.getStatusCode());
        Assert.assertNotNull("body is null", voidActual.getBody());

        //checking if ApiError.ACTIVE_BORROW_ORDERS. is returned in actual.getBody()
        Assert.assertEquals("wrong api error", Integer.valueOf(ApiError.ACTIVE_BORROW_ORDERS.getApiError()), voidActual.getBody().getApiError());
        Assert.assertEquals("wrong http status", Integer.valueOf(ApiError.ACTIVE_BORROW_ORDERS.getHttpStatus().value()), voidActual.getBody().getHttpError());
        Assert.assertEquals("wrong api error msg", ApiError.ACTIVE_BORROW_ORDERS.getApiErrorMsg(), voidActual.getBody().getApiErrorMsg());
    }
}
