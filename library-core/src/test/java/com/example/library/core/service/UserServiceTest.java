package com.example.library.core.service;

import com.example.library.core.exception.ActiveBorrowOrdersException;
import com.example.library.core.exception.UserExistException;
import com.example.library.core.exception.UserNotFoundException;
import com.example.library.core.repository.BorrowOrderRepository;
import com.example.library.core.repository.UserRepository;
import com.example.library.model.BorrowOrder;
import com.example.library.model.User;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@Slf4j
@RunWith(MockitoJUnitRunner.Silent.class)
public class UserServiceTest {

    private Long mockedId;
    private Long incorrectId;
    private String mockedUsername;
    private String usernameDoesNotExist;
    private User mockedUser;
    private User actualUser;
    private BorrowOrder mockedBorrowOrder;
    private UserRepository userRepositoryMock;
    private BorrowOrderRepository borrowOrderRepositoryMock;
    private List<User> actualUsersDbTable;
    private List<User> mockedUsersDbTable;
    private List<BorrowOrder> mockedBorrowOrdersDbTable;
    private Optional<User> mockedUserOpt;
    private UserService userService;

    @Before
    public void initialize() {
        mockedId = 1L;
        incorrectId = 2L;
        mockedUsername = "mockedUsername";
        usernameDoesNotExist = "usernameDoesNotExist";
        userRepositoryMock = mock(UserRepository.class);
        borrowOrderRepositoryMock = mock(BorrowOrderRepository.class);
        actualUser = new User();
        mockedUser = new User();
        mockedUser.setId(mockedId);
        mockedUser.setUsername(mockedUsername);
        mockedBorrowOrder = new BorrowOrder();
        mockedBorrowOrder.setUser(mockedUser);
        actualUsersDbTable = new ArrayList<>();
        mockedUsersDbTable = new ArrayList<>();
        mockedUsersDbTable.add(mockedUser);
        mockedBorrowOrdersDbTable =new ArrayList<>();
        mockedUserOpt = Optional.of(mockedUser);
        userService = new UserService(userRepositoryMock, borrowOrderRepositoryMock);
    }

    @Test
    public void registerUser_when_user_does_not_exist() {
        // given
        User mockedNewUser = new User();
        doAnswer(invocation -> {
            if (mockedUsersDbTable.contains(invocation.getArgument(0, User.class))) {
                throw UserExistException.createWithUsername(mockedNewUser.getUsername());
            } else {
                mockedUsersDbTable.add(mockedNewUser);
                return null;
            }
        }).when(userRepositoryMock).save(mockedNewUser);

        when(userRepositoryMock.findById(mockedId)).thenReturn(mockedUserOpt);

        // when
        try {
            userService.registerUser(mockedNewUser);
        } catch (UserExistException e) {
            log.error("User already exist", e);
            Assert.fail("User already exist");
            return;
        }

        // then
        boolean wasRegistered = mockedUsersDbTable.stream().anyMatch(mockedNewUser::equals);
        Assert.assertTrue("User wasn't registered", wasRegistered);
    }

    @Test(expected = UserExistException.class)
    public void registerUser_when_user_already_exist() throws UserExistException {
        // given
        mockedUsersDbTable.add(mockedUser);
        doAnswer(invocation -> {
            if (mockedUsersDbTable.contains(invocation.getArgument(0, User.class))) {
                throw UserExistException.createWithUsername(mockedUser.getUsername());
            } else {
                mockedUsersDbTable.add(mockedUser);
                return null;
            }
        }).when(userRepositoryMock).save(mockedUser);

        when(userRepositoryMock.findById(mockedId)).thenReturn(mockedUserOpt);

        // when then
        userService.registerUser(mockedUser);
    }

    @Test
    public void getUserById_when_user_exist() {
        // given
        when(userRepositoryMock.findById(mockedId)).thenReturn(mockedUserOpt);

        // when
        try {
            actualUser = userService.getUserById(mockedId);
        } catch (UserNotFoundException e) {
            log.error("User not found", e);
            Assert.fail("User not found");
            return;
        }

        // then
        Assert.assertEquals("User id is incorrect!", mockedId, actualUser.getId());
    }

    @Test(expected = UserNotFoundException.class)
    public void getUserById_when_user_does_not_exist() throws UserNotFoundException {
        // given
        when(userRepositoryMock.findById(mockedId)).thenReturn(mockedUserOpt);

        // when then
        actualUser = userService.getUserById(incorrectId);
    }

    @Test
    public void getUserByUsername_when_user_exist() {
        // given
        when(userRepositoryMock.findByUsername(mockedUsername)).thenReturn(mockedUserOpt);

        // when
        try {
            actualUser = userService.getUserByUsername(mockedUsername);
        } catch (UserNotFoundException e) {
            log.error("User not found", e);
            Assert.fail("User not found");
            return;
        }

        // then
        Assert.assertEquals("Username is incorrect", mockedUsername, actualUser.getUsername());
    }

    @Test(expected = UserNotFoundException.class)
    public void getUserByUsername_when_user_does_not_exist() throws UserNotFoundException {
        // given
        when(userRepositoryMock.findByUsername(mockedUsername)).thenReturn(mockedUserOpt);

        // when then
        userService.getUserByUsername(usernameDoesNotExist);
    }

    @Test
    public void getUsers_with_correct_list_size() {
        // given
        when(userRepositoryMock.findAll()).thenReturn(mockedUsersDbTable);

        // when
        actualUsersDbTable = userService.getUsers();

        // then
        Assert.assertEquals("Incorrect array size", mockedUsersDbTable.size(), actualUsersDbTable.size());

        //checking if elements in both lists are equal
        for (int i = 0; i < mockedUsersDbTable.size(); i++) {
            User expectedUser = mockedUsersDbTable.get(i);
            User actualUser = actualUsersDbTable.get(i);
            Assert.assertEquals(String.format("Element %s is not equal actual %s for i = %d", expectedUser, actualUser, i), expectedUser, actualUser);
        }
    }

    @Test
    public void getUsers_with_incorrect_list_size() {
        // given
        List<User> incorrectUserDbTable = new ArrayList<>();
        when(userRepositoryMock.findAll()).thenReturn(mockedUsersDbTable);

        // when
        actualUsersDbTable = userService.getUsers();

        // then
        Assert.assertNotEquals("Correct array size", incorrectUserDbTable.size(), actualUsersDbTable.size());
    }

    @Test
    public void deleteUserById_when_user_exist() {
        // given
        doAnswer(invocation -> {
            Long callId = invocation.getArgument(0, Long.class);
            mockedUsersDbTable.removeIf(user -> callId.equals(user.getId()));
            return null;
        }).when(userRepositoryMock).deleteById(mockedId);

        when(userRepositoryMock.findById(mockedId)).thenReturn(mockedUserOpt);

        // when
        try {
            userService.deleteUserById(mockedId);
        } catch (UserNotFoundException e) {
            log.error("User not found", e);
            Assert.fail("User not found");
            return;
        } catch (ActiveBorrowOrdersException a) {
            log.error("User has active borrow orders", a);
            Assert.fail("User has active borrow orders");
            return;
        }

        // then
        boolean wasRemoved = mockedUsersDbTable.stream().noneMatch(user -> mockedId.equals(user.getId()));
        Assert.assertTrue("It wasn't removed ", wasRemoved);
    }

    @Test(expected = UserNotFoundException.class)
    public void deleteUserById_when_user_does_not_exist() throws UserNotFoundException, ActiveBorrowOrdersException {
        // given
        doAnswer(invocation -> {
            Long callId = invocation.getArgument(0, Long.class);
            mockedUsersDbTable.removeIf(user -> callId.equals(user.getId()));
            return null;
        }).when(userRepositoryMock).deleteById(mockedId);

        when(userRepositoryMock.findById(mockedId)).thenReturn(mockedUserOpt);

        // when then
        userService.deleteUserById(incorrectId);
    }

    @Test(expected = ActiveBorrowOrdersException.class)
    public void deleteUserById_when_user_has_active_borrow_orders() throws UserNotFoundException, ActiveBorrowOrdersException {
        // given
        doAnswer(invocation -> {
            Long callId = invocation.getArgument(0, Long.class);
            mockedUsersDbTable.removeIf(user -> callId.equals(user.getId()));
            return null;
        }).when(userRepositoryMock).deleteById(mockedId);

        when(userRepositoryMock.findById(mockedId)).thenReturn(mockedUserOpt);
        mockedBorrowOrdersDbTable.add(mockedBorrowOrder);
        when(borrowOrderRepositoryMock.findAllByUserId(mockedId)).thenReturn(mockedBorrowOrdersDbTable);

        // when then
        userService.deleteUserById(mockedId);
    }

    @Test
    public void updateUserById_when_user_exist() {
        // given
        doAnswer(invocation -> {
            if (!mockedUsersDbTable.contains(invocation.getArgument(0, User.class))) {
                throw UserNotFoundException.createWithId(mockedUser.getId());
            } else {
                mockedUsersDbTable.add(0, mockedUser);
                return null;
            }
        }).when(userRepositoryMock).save(mockedUser);

        when(userRepositoryMock.findById(mockedId)).thenReturn(mockedUserOpt);

        // when
        try {
            userService.updateUser(mockedUser);
        } catch (UserNotFoundException e) {
            log.error("User not found", e);
            Assert.fail("User not found");
            return;
        }

        // then
        boolean wasUpdated = mockedUsersDbTable.stream().anyMatch(mockedUser::equals);
        Assert.assertTrue("User wasn't registered", wasUpdated);
    }

    @Test(expected = UserNotFoundException.class)
    public void updateUserById_when_user_does_not_exist() throws UserNotFoundException {
        // given
        doAnswer(invocation -> {
            if (!mockedUsersDbTable.contains(invocation.getArgument(0, User.class))) {
                throw UserNotFoundException.createWithId(mockedUser.getId());
            } else {
                mockedUsersDbTable.add(0, mockedUser);
                return null;
            }
        }).when(userRepositoryMock).save(mockedUser);

        when(userRepositoryMock.findById(incorrectId)).thenReturn(mockedUserOpt);

        // when then
        userService.updateUser(mockedUser);

    }
}
