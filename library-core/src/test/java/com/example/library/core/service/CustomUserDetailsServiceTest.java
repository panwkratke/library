package com.example.library.core.service;

import com.example.library.core.repository.UserRepository;
import com.example.library.model.User;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Slf4j
@RunWith(MockitoJUnitRunner.Silent.class)
public class CustomUserDetailsServiceTest {

    private String mockedUsername;
    private String mockedUsernameDoesNotExist;
    private Optional<User> mockedUserOptional;
    private UserRepository userRepositoryMock;
    private CustomUserDetailsService customUserDetailsService;

    @Before
    public void initialize() {
        mockedUsername = "mockedUsername";
        mockedUsernameDoesNotExist = "mockedUsernameDoesNotExist";
        User mockedUser = new User();
        mockedUser.setUsername(mockedUsername);
        mockedUserOptional = Optional.of(mockedUser);
        userRepositoryMock = mock(UserRepository.class);
        customUserDetailsService = new CustomUserDetailsService(userRepositoryMock);
    }

    @Test
    public void loadUserByUsername_when_user_exist() {
        //given
        when(userRepositoryMock.findByUsername(mockedUsername)).thenReturn(mockedUserOptional);

        //when
        UserDetails actualUserDetails;
        try {
            actualUserDetails = customUserDetailsService.loadUserByUsername(mockedUsername);
        } catch (UsernameNotFoundException e) {
            log.error("User not found", e);
            Assert.fail("User not found");
            return;
        }

        //then
        Assert.assertEquals("Username is incorrect", mockedUsername, actualUserDetails.getUsername());
    }

    @Test(expected = UsernameNotFoundException.class)
    public void loadUserByUsername_when_user_does_not_exist() throws UsernameNotFoundException {
        //given
        when(userRepositoryMock.findByUsername(mockedUsername)).thenReturn(mockedUserOptional);

        //when then
        customUserDetailsService.loadUserByUsername(mockedUsernameDoesNotExist);
    }
}
