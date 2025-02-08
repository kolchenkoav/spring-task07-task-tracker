package com.example.webfluxexample.web.controller;

import com.example.webfluxexample.AbstractTest;

import com.example.webfluxexample.controller.UserController;
import com.example.webfluxexample.model.UserModel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import reactor.core.publisher.Mono;

import java.util.List;

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class UserControllerTest extends AbstractTest {

    @Container
    static MongoDBContainer mongoDBContainer1 = new MongoDBContainer("mongo:6.0.8")
            .withReuse(true);
    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", mongoDBContainer1::getReplicaSetUrl);
    }

//    @Test
//    @WithMockUser(username = "user", roles = "USER")
//    public void whenUserRequestMethodWithUserRole_thenReturnOk() throws Exception {
//
//        mockMvc.perform(get("/api/v1/users"))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
//    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
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
    @WithMockUser(username = "user", roles = "USER")
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
    @WithMockUser(username = "user", roles = "USER")
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
    @WithMockUser(username = "user", roles = "USER")
    @DisplayName("When delete user, then return no content")
    public void whenDeleteUserThenReturnNoContent() {
        String userId = FIRST_USER_ID;

        webTestClient.delete().uri("/api/v1/users/{id}", userId)
                .exchange()
                .expectStatus().isNoContent();
    }
}
