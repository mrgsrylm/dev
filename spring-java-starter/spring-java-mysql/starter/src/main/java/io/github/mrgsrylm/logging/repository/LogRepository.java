package io.github.mrgsrylm.logging.repository;

import io.github.mrgsrylm.logging.entity.LogEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface for the {@link LogEntity} class.
 */
public interface LogRepository extends JpaRepository<LogEntity, Long> {
}
