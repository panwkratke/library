package com.example.library.core.service;

import com.example.library.core.exception.RoleNotFoundException;
import com.example.library.core.repository.RoleRepository;
import com.example.library.model.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Role service retrieves role from repository.
 */
@Service
public class RoleService {

    private final RoleRepository roleRepository;

    @Autowired
    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    /**
     * Retrieves the role from repository by role name.
     *
     * @param name Role name to be searched in the repository.
     * @return Role from the role repository with given role name.
     * @throws RoleNotFoundException The role with given name does not exist in the repository.
     */
    public Role getRoleByRoleName(String name) throws RoleNotFoundException {
        Optional<Role> roleOptional = this.roleRepository.findByName(name);
        if (roleOptional.isEmpty()) {
            throw RoleNotFoundException.createWithRoleName(name);
        }
        return roleOptional.get();
    }
}
