package com.example.webfluxexample.controller;

import com.example.webfluxexample.entity.Role;
import com.example.webfluxexample.entity.RoleType;
import com.example.webfluxexample.entity.User;
import com.example.webfluxexample.model.UserDto;
import com.example.webfluxexample.model.UserModel;
import com.example.webfluxexample.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Контроллер для управления пользователями.
 */
@Slf4j
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
    @PreAuthorize("hasAnyRole('USER', 'MANAGER')")
    @GetMapping
    public Flux<UserModel> getAllTasks() {
        return userService.findAll();
    }

    /**
     * Получает пользователя по идентификатору.
     *
     * @param id идентификатор пользователя.
     * @return Mono<ResponseEntity < UserModel>> пользователь.
     */
    @PreAuthorize("hasAnyRole('USER', 'MANAGER')")
    @GetMapping("/{id}")
    public Mono<ResponseEntity<UserModel>> getUserById(@PathVariable String id) {
        return userService.findById(id);
    }

    /**
     * Создает новый аккаунт пользователя.
     *
     * @param userDto  данные пользователя.
     * @param roleType тип роли.
     * @return Mono<ResponseEntity<UserDto>> данные созданного пользователя.
     */
    @PostMapping("/account")
    public Mono<ResponseEntity<UserDto>> createUserAccount(@RequestBody UserDto userDto,
                                                           @RequestParam RoleType roleType) {
        log.info("createUserAccount() userDto: {} roleType: {}", userDto, roleType);
        return createAccount(userDto, roleType)
                .map(userDtoResponse -> ResponseEntity.status(HttpStatus.CREATED).body(userDtoResponse));
    }

    /**
     * Создает учетную запись пользователя.
     *
     * @param userDto  данные пользователя.
     * @param roleType тип роли.
     * @return Mono<UserDto> данные созданного пользователя.
     */
    private Mono<UserDto> createAccount(UserDto userDto, RoleType roleType) {
        var user = new User();
        user.setPassword(userDto.getPassword());
        user.setUsername(userDto.getUsername());

        return userService.createNewAccount(user, Role.from(roleType))
                .map(createdUser -> UserDto.builder()
                        .username(createdUser.getUsername())
                        .password(createdUser.getPassword())
                        .build());
    }

    /**
     * Создает нового пользователя.
     *
     * @param userModel модель пользователя.
     * @return Mono<ResponseEntity < UserModel>> созданный пользователь.
     */
    @PreAuthorize("hasAnyRole('USER', 'MANAGER')")
    @PostMapping
    public Mono<ResponseEntity<UserModel>> createUser(@RequestBody UserModel userModel) {
        return userService.save(userModel).log();
    }

    /**
     * Обновляет существующего пользователя.
     *
     * @param id        идентификатор пользователя.
     * @param userModel модель пользователя.
     * @return Mono<ResponseEntity < UserModel>> обновленный пользователь.
     */
    @PreAuthorize("hasAnyRole('USER', 'MANAGER')")
    @PutMapping("/{id}")
    public Mono<ResponseEntity<UserModel>> updateUser(@PathVariable String id,
                                                      @RequestBody UserModel userModel) {
        return userService.update(id, userModel);
    }

    /**
     * Удаляет пользователя по идентификатору.
     *
     * @param id идентификатор пользователя.
     * @return Mono<ResponseEntity < Void>> результат удаления.
     */
    @PreAuthorize("hasAnyRole('USER', 'MANAGER')")
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteUser(@PathVariable String id) {
        return userService.deleteById(id);
    }
}
