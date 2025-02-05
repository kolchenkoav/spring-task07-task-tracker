package com.example.webfluxexample.controller;

import com.example.webfluxexample.model.UserModel;
import com.example.webfluxexample.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Контроллер для управления пользователями.
 */
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    /**
     * Получает всех пользователей.
     *
     * @return Flux<UserModel> список всех пользователей.
     */
    @GetMapping
    public Flux<UserModel> getAllTasks() {
        return userService.findAll();
    }

    /**
     * Получает пользователя по идентификатору.
     *
     * @param id идентификатор пользователя.
     * @return Mono<ResponseEntity<UserModel>> пользователь.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<UserModel>> getUserById(@PathVariable String id) {
        return userService.findById(id);
    }

    /**
     * Получает пользователя по имени.
     *
     * @param name имя пользователя.
     * @return Mono<ResponseEntity<UserModel>> пользователь.
     */
    @GetMapping("/by-name")
    public Mono<ResponseEntity<UserModel>> getByUsername(@RequestParam String name) {
        return userService.findByUsername(name);
    }

    /**
     * Создает нового пользователя.
     *
     * @param userModel модель пользователя.
     * @return Mono<ResponseEntity<UserModel>> созданный пользователь.
     */
    @PostMapping
    public Mono<ResponseEntity<UserModel>> createUser(@RequestBody UserModel userModel) {
        return userService.save(userModel).log();
    }

    /**
     * Обновляет существующего пользователя.
     *
     * @param id идентификатор пользователя.
     * @param userModel модель пользователя.
     * @return Mono<ResponseEntity<UserModel>> обновленный пользователь.
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<UserModel>> updateUser(@PathVariable String id,
                                                      @RequestBody UserModel userModel) {
        return userService.update(id, userModel);
    }

    /**
     * Удаляет пользователя по идентификатору.
     *
     * @param id идентификатор пользователя.
     * @return Mono<ResponseEntity<Void>> результат удаления.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteUser(@PathVariable String id) {
        return userService.deleteById(id);
    }
}
