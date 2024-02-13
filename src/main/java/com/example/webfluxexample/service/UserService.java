package com.example.webfluxexample.service;

import com.example.webfluxexample.entity.User;
import com.example.webfluxexample.mapper.UserMapper;
import com.example.webfluxexample.model.UserModel;
import com.example.webfluxexample.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    private final UserMapper userMapper;

    public Flux<UserModel> findAll() {
        return userRepository.findAll().map(userMapper::toModel);
    }

    public Mono<ResponseEntity<UserModel>> findById(String id) {
        return userRepository.findById(id)
                .map(userMapper::toModel)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    public Mono<ResponseEntity<UserModel>> findByUsername(String name) {
        return userRepository.findByUsername(name)
                .map(userMapper::toModel)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    public Mono<ResponseEntity<UserModel>> save(UserModel userModel) {
        userModel.setId(UUID.randomUUID().toString());

        Mono<User> user = userRepository.save(userMapper.toEntity(userModel));

        UserModel userModel1 = userMapper.toModel(user.blockOptional().orElse(null));

        ResponseEntity<UserModel> userModelResponseEntity = ResponseEntity.ok(userModel1);

        Mono<UserModel> userModelMono = Mono.just(userModel1);

//        Mono<ResponseEntity<UserModel>> responseEntityMono = userModelMono
//                .map(ResponseEntity::ok)
//                .defaultIfEmpty(ResponseEntity.ok().build());

        return Mono.just(userModelResponseEntity);
    }

//    public Mono<User> update(String id, User user) {
//        return findById(id).flatMap(itemForUpdate -> {
//            if (StringUtils.hasText(user.getUsername())) {
//                itemForUpdate.setUsername(user.getUsername());
//                itemForUpdate.setEmail(user.getEmail());
//            }
//
////            if (item.getCount() != null) {
////                itemForUpdate.setCount(item.getCount());
////            }
////            if (item.getSubItems() != null) {
////                itemForUpdate.setSubItems(item.getSubItems());
////            }
//            return itemRepository.save(itemForUpdate);
//        });
//    }

    public Mono<Void> deleteById(String id) {
        return userRepository.deleteById(id);
    }
}
