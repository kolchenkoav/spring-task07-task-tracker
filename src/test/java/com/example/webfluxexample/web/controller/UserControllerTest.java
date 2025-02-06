package com.example.webfluxexample.web.controller;

import com.example.webfluxexample.AbstractTest;

import com.example.webfluxexample.model.UserModel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.http.MediaType;
import reactor.core.publisher.Mono;

import java.util.List;

public class UserControllerTest extends AbstractTest {

    @Test
    @DisplayName("When get all users, then return list of users from database")
    public void whenGetAllUsersThenReturnListOfUsersFromDatabase() {
        var expectedData = List.of(
                new UserModel(FIRST_USER_ID,
                        "First_user",
                        "First_user@some.com"
                ),
                new UserModel(SECOND_USER_ID,
                        "Second_user",
                        "Second_user@some.com"
                ),
                new UserModel(THIRD_USER_ID,
                        "Third_user",
                        "Third_user@some.com"
                ),
                new UserModel(FOURTH_USER_ID,
                        "Fourth_user",
                        "Fourth_user@some.com"
                ),
                new UserModel(AUTHOR_USER_ID,
                        "Author_user",
                        "Author_user@some.com"
                ),
                new UserModel(ASSIGNEE_USER_ID,
                        "Assignee_user",
                        "Assignee_user@some.com"
                )
        );

        webTestClient.get().uri("/api/v1/users")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(UserModel.class)
                .hasSize(6)
                .contains(expectedData.toArray(UserModel[]::new));
    }

    @Test
    @DisplayName("When create user, then return created user")
    public void whenCreateUserThenReturnCreatedUser() {
        UserModel userModel = new UserModel(null, "new_user", "user@example.com");

        webTestClient.post().uri("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(userModel), UserModel.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody(UserModel.class)
                .consumeWith(response -> {
                    UserModel createdUser = response.getResponseBody();
                    assert createdUser != null;
                    assert createdUser.getUsername().equals("new_user");
                    assert createdUser.getEmail().equals("user@example.com");
                });
    }

    @Test
    @DisplayName("When update user, then return updated user")
    public void whenUpdateUserThenReturnUpdatedUser() {
        String userId = FIRST_USER_ID;
        UserModel updatedUserModel = new UserModel(FIRST_USER_ID, "First_user", "First_user@some.com");

        webTestClient.put().uri("/api/v1/users/{id}", userId)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(updatedUserModel), UserModel.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody(UserModel.class)
                .consumeWith(response -> {
                    UserModel updatedUser = response.getResponseBody();
                    assert updatedUser != null;
                    assert updatedUser.getId().equals(userId);
                    assert updatedUser.getUsername().equals("First_user");
                    assert updatedUser.getEmail().equals("First_user@some.com");
                });
    }

    @Test
    @DisplayName("When delete user, then return no content")
    public void whenDeleteUserThenReturnNoContent() {
        String userId = "some_user_id";

        webTestClient.delete().uri("/api/v1/users/{id}", userId)
                .exchange()
                .expectStatus().isNoContent();
    }
}
