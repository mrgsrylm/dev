package io.github.mrgsrylm.exception.custom;

import io.github.mrgsrylm.exception.AlreadyException;

import java.io.Serial;

public class UsernameAlreadyExistsException extends AlreadyException {
    @Serial
    private static final long serialVersionUID = 1L;

    private static final String DEFAULT_MESSAGE =
            "The specified username already exists";

    private static final String MESSAGE_TEMPLATE =
            "Username already exists: ";

    public UsernameAlreadyExistsException(String email) {
        super(MESSAGE_TEMPLATE.concat(email));
    }

    public UsernameAlreadyExistsException() {
        super(DEFAULT_MESSAGE);
    }
}
