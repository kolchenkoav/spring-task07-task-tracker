package com.example.webfluxexample.repository;

import com.example.webfluxexample.entity.User;
import com.example.webfluxexample.model.UserModel;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface UserRepository extends ReactiveMongoRepository<User, String> {
    Mono<User> findByUsername(String name);

    Mono<UserModel> save(User user);
}
