package com.example.webfluxexample.service;

import com.example.webfluxexample.entity.User;
import com.example.webfluxexample.mapper.UserMapper;
import com.example.webfluxexample.model.UserModel;
import com.example.webfluxexample.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * Сервис для работы с пользователями.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    /**
     * Найти всех пользователей.
     *
     * @return Поток моделей пользователей.
     */
    public Flux<UserModel> findAll() {
        return userRepository.findAll().map(userMapper::toModel);
    }

    /**
     * Найти пользователя по идентификатору.
     *
     * @param id Идентификатор пользователя.
     * @return Монада с ответом, содержащим модель пользователя или статус 404, если пользователь не найден.
     */
    public Mono<ResponseEntity<UserModel>> findById(String id) {
        return userRepository.findById(id)
                .map(userMapper::toModel)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    /**
     * Найти пользователя по имени пользователя.
     *
     * @param name Имя пользователя.
     * @return Монада с ответом, содержащим модель пользователя или статус 404, если пользователь не найден.
     */
    public Mono<ResponseEntity<UserModel>> findByUsername(String name) {
        return userRepository.findByUsername(name)
                .map(userMapper::toModel)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    /**
     * Сохранить нового пользователя.
     *
     * @param userModel Модель пользователя для сохранения.
     * @return Монада с ответом, содержащим сохраненную модель пользователя.
     */
    public Mono<ResponseEntity<UserModel>> save(UserModel userModel) {
        userModel.setId(UUID.randomUUID().toString());
        Mono<User> user = userRepository.save(userMapper.toEntity(userModel));

        return user.map(userMapper::toModel).cast(UserModel.class)
                //.doOnSuccess(userUpdatesPublisher::publish)
                .map(ResponseEntity::ok);
    }

    /**
     * Обновить существующего пользователя.
     *
     * @param id Идентификатор пользователя для обновления.
     * @param userModel Модель пользователя с обновленными данными.
     * @return Монада с ответом, содержащим обновленную модель пользователя или статус 404, если пользователь не найден.
     */
    public Mono<ResponseEntity<UserModel>> update(String id, UserModel userModel) {
//        return userRepository.findById(id).flatMap(userForUpdate -> {
//            User user = userMapper.toEntity(userModel);
//
//            if (StringUtils.hasText(user.getUsername())) {
//                userForUpdate.setUsername(user.getUsername());
//            }
//            if (StringUtils.hasText(user.getEmail())) {
//                userForUpdate.setEmail(user.getEmail());
//            }
//
//            return userRepository.save(userForUpdate).map(userMapper::toModel)
//                    .map(ResponseEntity::ok)
//                    .defaultIfEmpty(ResponseEntity.notFound().build());
//        });
        return userRepository.findById(id)
                .flatMap(existingUser -> {
                    existingUser.setUsername(userModel.getUsername());
                    existingUser.setEmail(userModel.getEmail());
                    return userRepository.save(existingUser);
                })
                .map(userMapper::toModel)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    /**
     * Удалить пользователя по идентификатору.
     *
     * @param id Идентификатор пользователя для удаления.
     * @return Монада с ответом, содержащим статус 204, если пользователь успешно удален.
     */
    public Mono<ResponseEntity<Void>> deleteById(String id) {
        return userRepository.deleteById(id).log()
                .then(Mono.just(ResponseEntity.noContent().build()));
    }
}
