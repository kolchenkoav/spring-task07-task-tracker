package com.example.webfluxexample.publisher;

import com.example.webfluxexample.model.UserModel;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Sinks;

@Component
public class UserUpdatesPublisher {
    private final Sinks.Many<UserModel> userModelUpdatesSink;

    public UserUpdatesPublisher() {
        this.userModelUpdatesSink = Sinks.many().multicast().onBackpressureBuffer();
    }

    public void publish(UserModel userModel) {
        userModelUpdatesSink.tryEmitNext(userModel);
    }

    public Sinks.Many<UserModel> getUpdatesSink() {
        return userModelUpdatesSink;
    }
}
