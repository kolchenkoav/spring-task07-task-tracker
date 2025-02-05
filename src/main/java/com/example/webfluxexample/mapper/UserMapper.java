package com.example.webfluxexample.mapper;

import com.example.webfluxexample.entity.User;
import com.example.webfluxexample.model.UserModel;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.util.ArrayList;
import java.util.List;

/**
 * Маппер для преобразования между моделями UserModel и User.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = {UserModel.class, User.class})
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    /**
     * Преобразует UserModel в User.
     *
     * @param userModel модель пользователя
     * @return сущность пользователя
     */
    User toEntity(UserModel userModel);

    /**
     * Преобразует User в UserModel.
     *
     * @param user сущность пользователя
     * @return модель пользователя
     */
    UserModel toModel(User user);

    /**
     * Преобразует список UserModel в список User.
     *
     * @param userModelList список моделей пользователей
     * @return список сущностей пользователей
     */
    default List<User> toEntityList(List<UserModel> userModelList) {
        return new ArrayList<>(userModelList.stream()
                .map(this::toEntity).toList());
    }

    /**
     * Преобразует список User в список UserModel.
     *
     * @param userList список сущностей пользователей
     * @return список моделей пользователей
     */
    default List<UserModel> toModelList(List<User> userList) {
        return new ArrayList<>(userList.stream()
                .map(this::toModel).toList());
    }
}
