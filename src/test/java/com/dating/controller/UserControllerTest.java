package com.dating.controller;

import com.dating.model.User;
import com.dating.service.UserService;
import com.dating.service.ZodiacCompatibilityService;
import com.dating.utility.SecurityConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@WebFluxTest({UserController.class, CompatibilityController.class})
@Import(SecurityConfig.class)
class UserControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private ZodiacCompatibilityService zodiacCompatibilityService;

    private User user;

    @BeforeEach
    void setup() {
        user = new User("13252350sgew353tyugh", "love_hurts943@example.com", "love_hurts943",
                "", "cuew2431pis3e02fgsg", "female", Arrays.asList("singing", "hiking"),
                "gemini", List.of());
    }

    @Test
    void shouldCreateNewUser() {
        when(userService.saveUser(any(User.class))).thenReturn(Mono.just(user));

        webTestClient.post()
                .uri("/userapi/addUser")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(user)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.id").isEqualTo(user.id());
    }

    @Test
    void shouldGetUsers() {
        when(userService.getAllUsers()).thenReturn(Flux.just(user));

        webTestClient.get()
                .uri("/userapi/users")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$").isArray();
    }

    @Test
    void shouldReturnNotFoundWhenThereAreNoUsers() {
        when(userService.getAllUsers()).thenReturn(Flux.empty());

        webTestClient.get()
                .uri("/userapi/users")
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void shouldGetUserById() {
        when(userService.getUserById(user.id())).thenReturn(Mono.just(user));

        webTestClient.get()
                .uri("/userapi/user/{id}", user.id())
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.id").isEqualTo(user.id());
    }

    @Test
    void shouldGetUserByUserName() {
        when(userService.findUserByUserName(user.userName())).thenReturn(Mono.just(user));

        webTestClient.get()
                .uri("/userapi/user/name/{userName}", user.userName())
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.userName").isEqualTo(user.userName());
    }

    @Test
    void shouldGetUsersByZodiacSign() {
        User secondUser = new User(null, "love_cures84@example.com", "love_cures84",
                "", "valentin3252%$%", "male", List.of("singing", "hiking"),
                "gemini", List.of());
        when(userService.getUsersByZodiacSign("gemini")).thenReturn(Flux.just(user, secondUser));

        webTestClient.get()
                .uri("/userapi/user/zodiac/{sign}", "gemini")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$").isArray()
                .jsonPath("$.length()").isEqualTo(2);
    }

    @Test
    void shouldGetCompatibilityScore() {
        when(zodiacCompatibilityService.getCompatibilityScore("aries", "leo"))
                .thenReturn(Mono.just(90));

        webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path("/userapi/compatibility")
                        .queryParam("first", "aries")
                        .queryParam("second", "leo")
                        .build())
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.score").isEqualTo(90);
    }

    @Test
    void shouldReturnTestString() {
        webTestClient.get()
                .uri("/userapi/test")
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class).isEqualTo("API is working!");
    }
}
