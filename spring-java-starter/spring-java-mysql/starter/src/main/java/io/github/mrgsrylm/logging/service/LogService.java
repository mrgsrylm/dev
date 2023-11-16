package io.github.mrgsrylm.logging.service;

import io.github.mrgsrylm.logging.entity.LogEntity;

/**
 * Service interface for saving {@link LogEntity} to the database.
 */
public interface LogService {
    void saveLogToDatabase(LogEntity logEntity);
}
