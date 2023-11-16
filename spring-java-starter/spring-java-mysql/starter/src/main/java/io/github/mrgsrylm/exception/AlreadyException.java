package io.github.mrgsrylm.exception;

import org.springframework.http.HttpStatus;

import java.io.Serial;

public abstract class AlreadyException extends RuntimeException {
    public static final HttpStatus STATUS = HttpStatus.CONFLICT;
    @Serial
    private static final long serialVersionUID = 1L;

    protected AlreadyException(String message) {
        super(message);
    }
}
