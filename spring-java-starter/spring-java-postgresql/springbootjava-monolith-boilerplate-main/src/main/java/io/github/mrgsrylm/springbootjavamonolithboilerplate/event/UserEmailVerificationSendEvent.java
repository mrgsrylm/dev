package io.github.mrgsrylm.springbootjavamonolithboilerplate.event;

import io.github.mrgsrylm.springbootjavamonolithboilerplate.entity.User;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class UserEmailVerificationSendEvent extends ApplicationEvent {
    private final User user;

    public UserEmailVerificationSendEvent(Object source, User user) {
        super(source);
        this.user = user;
    }
}
