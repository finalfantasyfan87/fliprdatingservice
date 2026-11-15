package com.dating.exception;

import com.dating.controller.UserController;
import com.dating.model.User;
import com.dating.service.UserService;
import com.dating.utility.SecurityConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static org.mockito.Mockito.when;

@WebFluxTest(UserController.class)
@Import(SecurityConfig.class)
class GlobalExceptionHandlerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private UserService userService;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User("123", "love_hurts943@example.com", "love_hurts943",
                null, null, "female", null, "gemini", null);
    }

    @Test
    @DisplayName("GET /userapi/user/{id} returns ApiErrorResponse for missing user")
    void getUserByIdReturnsApiErrorResponseWhenUserNotFound() {
        when(userService.getUserById("missing-id")).thenReturn(Mono.empty());

        webTestClient.get().uri("/userapi/user/{id}", "missing-id")
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("$.code").isEqualTo("NOT_FOUND")
                .jsonPath("$.message").isEqualTo("User was not found");
    }

    @Test
    @DisplayName("POST /userapi/addUser returns ApiErrorResponse for invalid email")
    void addUserReturnsApiErrorResponseForInvalidEmail() {
        user = new User(user.id(), "not-an-email", user.userName(), user.profilePictureUrl(),
                user.password(), user.gender(), user.interests(), user.zodiacSign(), user.conversationIds());

        webTestClient.post().uri("/userapi/addUser")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(user)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.code").isEqualTo("BAD_REQUEST")
                .jsonPath("$.message").isEqualTo("A valid email address is required");
    }
}
