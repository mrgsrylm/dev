package io.github.mrgsrylm.springbootjavamonolithboilerplate.event;

import io.github.mrgsrylm.springbootjavamonolithboilerplate.entity.User;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class UserPasswordResetSendEvent extends ApplicationEvent {
    private final User user;

    public UserPasswordResetSendEvent(Object source, User user) {
        super(source);
        this.user = user;
    }
}
