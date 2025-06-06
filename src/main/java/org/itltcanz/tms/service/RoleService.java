package org.itltcanz.tms.service;

import lombok.RequiredArgsConstructor;
import org.itltcanz.tms.entity.RoleEntity;
import org.itltcanz.tms.exceptions.EntityException;
import org.itltcanz.tms.repository.RoleRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoleService {

    private final RoleRepository roleRepository;
    public RoleEntity findUserRole() {
        return roleRepository.findRoleByName("ROLE_USER").orElseThrow(() -> new EntityException("Role not found"));
    }

    public RoleEntity findAdminRole() {
        return roleRepository.findRoleByName("ROLE_ADMIN").orElseThrow(() -> new EntityException("Role not found"));
    }
}
