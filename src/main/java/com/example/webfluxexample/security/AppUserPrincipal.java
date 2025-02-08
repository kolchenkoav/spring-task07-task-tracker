package com.example.webfluxexample.security;

import com.example.webfluxexample.entity.Role;
import com.example.webfluxexample.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

/**
 * Класс, представляющий пользователя для Spring Security.
 */
@RequiredArgsConstructor
public class AppUserPrincipal implements UserDetails {

    private final User user;

    /**
     * Возвращает коллекцию ролей пользователя.
     *
     * @return коллекция ролей пользователя.
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return user.getRoles().stream().map(Role::toAuthority).toList();
    }

    /**
     * Возвращает пароль пользователя.
     *
     * @return пароль пользователя.
     */
    @Override
    public String getPassword() {
        return user.getPassword();
    }

    /**
     * Возвращает имя пользователя.
     *
     * @return имя пользователя.
     */
    @Override
    public String getUsername() {
        return user.getUsername();
    }

    /**
     * Проверяет, не истек ли срок действия аккаунта.
     *
     * @return true, если аккаунт не истек, иначе false.
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * Проверяет, не заблокирован ли аккаунт.
     *
     * @return true, если аккаунт не заблокирован, иначе false.
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * Проверяет, не истекли ли учетные данные.
     *
     * @return true, если учетные данные не истекли, иначе false.
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * Проверяет, включен ли аккаунт.
     *
     * @return true, если аккаунт включен, иначе false.
     */
    @Override
    public boolean isEnabled() {
        return true;
    }
}
