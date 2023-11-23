package io.github.mrgsrylm.springbootjavamonolithboilerplate.repository;

import io.github.mrgsrylm.springbootjavamonolithboilerplate.entity.Setting;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SettingRepository extends JpaRepository<Setting, UUID> {
}
