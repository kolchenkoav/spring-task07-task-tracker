package com.example.webfluxexample.handler;

import com.example.webfluxexample.entity.Task;
import com.example.webfluxexample.entity.User;
import com.example.webfluxexample.repository.UserRepository;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.reactivestreams.Subscription;
import reactor.core.publisher.BaseSubscriber;
import reactor.core.publisher.Mono;

@Getter
@RequiredArgsConstructor
//@NoArgsConstructor
public class TaskSubscriber<T> extends BaseSubscriber<Task> {
//    private User user = new User();
//    private final UserRepository userRepository;
//
//    public TaskSubscriber() {
//    }
//
//    public TaskSubscriber(UserRepository userRepository) {
//        this.userRepository = userRepository;
//    }
//
//    @Override
//    public void hookOnSubscribe(Subscription subscription) {
//        System.out.println("Subscribed");
//        request(1);
//    }
//
//    @Override
//    public void hookOnNext(Task value) {
//        Mono<User> userMono = userRepository.findById(value.getAuthorId());
//        userMono.subscribe(System.out::println);
//        System.out.println(value.getAuthorId());
//
//        request(1);
//    }
}
