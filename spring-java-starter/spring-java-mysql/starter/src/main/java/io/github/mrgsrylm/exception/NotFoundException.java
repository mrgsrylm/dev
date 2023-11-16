package io.github.mrgsrylm.exception;

import org.springframework.http.HttpStatus;

import java.io.Serial;

public abstract class NotFoundException extends RuntimeException {
    public static final HttpStatus STATUS = HttpStatus.NOT_FOUND;
    @Serial
    private static final long serialVersionUID = 1L;

    protected NotFoundException(String message) {
        super(message);
    }
}


