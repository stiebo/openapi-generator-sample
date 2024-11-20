package dev.stiebo.openapi_generator_sample.repository;

import dev.stiebo.openapi_generator_sample.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role,Long> {
    Role findByName(String name);
}
