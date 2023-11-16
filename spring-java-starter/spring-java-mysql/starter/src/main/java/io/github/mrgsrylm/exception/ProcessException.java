package io.github.mrgsrylm.exception;

import org.springframework.http.HttpStatus;

import java.io.Serial;

public abstract class ProcessException extends RuntimeException {
    public static final HttpStatus STATUS = HttpStatus.INTERNAL_SERVER_ERROR;
    @Serial
    private static final long serialVersionUID = 1L;

    protected ProcessException(String message) {
        super(message);
    }
}


