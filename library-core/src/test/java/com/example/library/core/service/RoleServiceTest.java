package com.example.library.core.service;

import com.example.library.core.exception.RoleNotFoundException;
import com.example.library.core.repository.RoleRepository;
import com.example.library.model.Role;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Slf4j
@RunWith(MockitoJUnitRunner.Silent.class)
public class RoleServiceTest {

    private String mockedRoleName;
    private String mockedRoleNameDoesNotExist;
    private Role actualRole;
    private Optional<Role> mockedRoleOptional;
    private RoleRepository roleRepositoryMock;
    private RoleService roleService;

    @Before
    public void initialize() {
        mockedRoleName = "mockedRoleName";
        mockedRoleNameDoesNotExist = "mockedRoleNameDoesNotExist";
        actualRole = new Role();
        Role mockedRole = new Role();
        mockedRole.setName(mockedRoleName);
        mockedRoleOptional = Optional.of(mockedRole);
        roleRepositoryMock = mock(RoleRepository.class);
        roleService = new RoleService(roleRepositoryMock);
    }

    @Test
    public void getRoleByRoleName_when_role_exist() {
        //given
        when(roleRepositoryMock.findByName(mockedRoleName)).thenReturn(mockedRoleOptional);

        //when
        try {
            actualRole = roleService.getRoleByRoleName(mockedRoleName);
        } catch (RoleNotFoundException e) {
            log.error("Role not found", e);
            Assert.fail("Role not found");
            return;
        }

        //then
        Assert.assertEquals("Role name is incorrect", mockedRoleName, actualRole.getName());
    }

    @Test(expected = RoleNotFoundException.class)
    public void getRoleByRoleName_when_role_does_not_exist() throws RoleNotFoundException {
        //given
        when(roleRepositoryMock.findByName(mockedRoleName)).thenReturn(mockedRoleOptional);

        //when then
        roleService.getRoleByRoleName(mockedRoleNameDoesNotExist);
    }
}
