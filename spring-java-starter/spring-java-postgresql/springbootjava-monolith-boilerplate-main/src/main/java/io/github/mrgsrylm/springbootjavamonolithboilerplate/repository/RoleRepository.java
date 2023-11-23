package io.github.mrgsrylm.springbootjavamonolithboilerplate.repository;

import io.github.mrgsrylm.springbootjavamonolithboilerplate.entity.Role;
import io.github.mrgsrylm.springbootjavamonolithboilerplate.util.Constants;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, String> {
    Optional<Role> findByName(Constants.RoleEnum name);
}
