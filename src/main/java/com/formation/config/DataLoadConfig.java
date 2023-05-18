package com.formation.config;


import com.formation.enums.RoleName;
import com.formation.models.Role;
import com.formation.repository.RoleRepository;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class DataLoadConfig {
    private final RoleRepository roleRepository;


    public DataLoadConfig(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @PostConstruct
    public void loadData() {
        loadRoles();
    }

    public void loadRoles() {
        for (RoleName roleName : RoleName.values()) {
            boolean exist=roleRepository.existsByName(roleName);
            if(!exist)
            {
                roleRepository.save(new Role(roleName));
            }
        }
    }

}
