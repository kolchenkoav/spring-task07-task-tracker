package com.example.webfluxexample.controller;

import com.example.webfluxexample.model.UserModel;
import com.example.webfluxexample.publisher.UserUpdatesPublisher;
import com.example.webfluxexample.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final UserUpdatesPublisher publisher;

    @GetMapping
    public Flux<UserModel> getAllItems() {
        return userService.findAll();
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<UserModel>> getById(@PathVariable String id) {
        return userService.findById(id);
    }

    @GetMapping("/by-name")
    public Mono<ResponseEntity<UserModel>> getByUsername(@RequestParam String name) {
        return userService.findByUsername(name);

    }

    @PostMapping
    public Mono<UserModel> createItem(@RequestBody UserModel userModel) {
        return userService.save(userModel).log();

    }
//
//    @PutMapping("/{id}")
//    public Mono<ResponseEntity<ItemModel>> updateItem(@PathVariable String id,
//                                                                    @RequestBody ItemModel itemModel) {
//        return itemService.update(id, Item.from(itemModel))
//                .map(ItemModel::from)
//                .map(ResponseEntity::ok)
//                .defaultIfEmpty(ResponseEntity.notFound().build());
//    }
//
//    @DeleteMapping("/{id}")
//    public Mono<ResponseEntity<Void>> deleteItem(@PathVariable String id) {
//        return itemService.deleteById(id).then(Mono.just(ResponseEntity.noContent().build()));
//    }
//
//    @GetMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
//    public Flux<ServerSentEvent<ItemModel>> getItemUpdates() {
//        return publisher.getUpdatesSink()
//                .asFlux()
//                .map(item -> ServerSentEvent.builder(item).build());
//    }
}
