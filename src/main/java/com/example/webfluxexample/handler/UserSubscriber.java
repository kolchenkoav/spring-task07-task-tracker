package com.example.webfluxexample.handler;

import com.example.webfluxexample.entity.User;
import lombok.Getter;
import org.reactivestreams.Subscription;
import reactor.core.publisher.BaseSubscriber;

//@Getter
public class UserSubscriber<T> extends BaseSubscriber<User> {
    //private User user = new User();

    @Override
    public void hookOnSubscribe(Subscription subscription) {
        System.out.println("==> Subscribed");
        request(1);
    }

    @Override
    public void hookOnNext(User user) {
        //this.user = user;
        System.out.println(user.getId());
        request(1);
    }
}