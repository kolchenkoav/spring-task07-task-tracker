package com.example.webfluxexample.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class UserHandler {
//    public Mono<ServerResponse> getAllItem(ServerRequest serverRequest) {
//        return ServerResponse.ok()
//                .contentType(MediaType.APPLICATION_JSON)
//                .body(Flux.just(
//                        new ItemModel(UUID.randomUUID().toString(), "Name 1", 10, Collections.emptyList()),
//                        new ItemModel(UUID.randomUUID().toString(), "Name 2", 20, List.of(
//                                new SubItemModel("SubItem 1", BigDecimal.valueOf(1001)),
//                                new SubItemModel("SubItem 2", BigDecimal.valueOf(2001))
//                        ))), ItemModel.class);
//    }
//
//    public Mono<ServerResponse> findById(ServerRequest serverRequest) {
//        return ServerResponse.ok()
//                .contentType(MediaType.APPLICATION_JSON)
//                .body(Mono.just(
//                                new ItemModel(serverRequest.pathVariable("id"),
//                                        "Item name 1", 10, Collections.emptyList())),
//                        ItemModel.class);
//    }
//
//    public Mono<ServerResponse> createItem(ServerRequest serverRequest) {
//        return serverRequest.bodyToMono(ItemModel.class)
//                .flatMap(item -> {
//                    log.info("Item for create: {}", item);
//                    return Mono.just(item);
//                })
//                .flatMap(item -> ServerResponse.created(URI.create("/api/v1/functions/item/" + item.getId())).build());
//    }
//
//    public Mono<ServerResponse> errorRequest(ServerRequest serverRequest) {
//        return ServerResponse.ok()
//                .contentType(MediaType.APPLICATION_JSON)
//                .body(Mono.error(new RuntimeException("Exception in errorRequest")), String.class)
//                .onErrorResume(ex -> {
//                    log.error("Error in errorRequest", ex);
//                    return ServerResponse.badRequest().body(Mono.error(ex), String.class);
//                });
//    }
}
