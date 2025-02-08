package com.example.webfluxexample.security;

import com.example.webfluxexample.entity.User;
import com.example.webfluxexample.model.UserModel;
import com.example.webfluxexample.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.HashSet;

/**
 * Реализация ReactiveUserDetailsService, которая использует UserService для поиска пользователей.
 * Этот сервис активен только при условии, что свойство app.security.type имеет значение "db".
 */
@Service
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "app.security", name = "type", havingValue = "db")
public class ReactiveUserDetailsServiceImpl implements ReactiveUserDetailsService {

    private final UserService userService;

    @Override
    public Mono<UserDetails> findByUsername(String username) {
        return userService.findByUsername(username)
                .map(AppUserPrincipal::new)
                .cast(UserDetails.class)
                .switchIfEmpty(Mono.error(new UsernameNotFoundException("User not found: " + username)));
    }

//    @Override
//    public Mono<UserDetails> findByUsername(String username) {
//        return userService.findByUsername(username)
//                .map(AppUserPrincipal::new)
//                .switchIfEmpty(Mono.error(new UsernameNotFoundException("User not found: " + username)));
//    }

//    @Override
//    public Mono<UserDetails> findByUsername(String username) {
//        return userService.findByUsername(username)
//                .flatMap(response -> {
//                    if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
//                        UserModel userModel = response.getBody();
//                        User user = new User(userModel.getId(), userModel.getUsername(), userModel.getEmail(), "", new HashSet<>()); // Конвертируем UserModel в User
//                        return Mono.just(new AppUserPrincipal(user));
//                    } else {
//                        return Mono.error(new UsernameNotFoundException("User not found: " + username));
//                    }
//                });
//    }

//    @Override
//    public Mono<UserDetails> findByUsername(String username) {
//        return userService.findByUsername(username)
//                .flatMap(response -> {
//                    if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
//                        UserModel userModel = response.getBody();
//                        User user = userMapper.toEntity(userModel); // Map UserModel to User
//                        return Mono.just(new AppUserPrincipal(user));
//                    } else {
//                        return Mono.error(new UsernameNotFoundException("User not found: " + username));
//                    }
//                });
//    }

//    @Override
//    public Mono<UserDetails> findByUsername(String username) {
//        return userService.findByUsername(username)
//                .map(AppUserPrincipal::new)
//                .switchIfEmpty(Mono.error(new UsernameNotFoundException("User not found: " + username)));
//    }

//    @Override
//    public Mono<UserDetails> findByUsername(String username) {
//        return userService.findByUsername(username)
//                .map(user -> new AppUserPrincipal(user));
//    }

//    @Override
//    public Mono<UserDetails> findByUsername(String username) {
//        return Mono.fromCallable(() -> userService.findByUsername(username))
//                .flatMap(Mono::just)
//                .map(AppUserPrincipal::new);
//    }
}
