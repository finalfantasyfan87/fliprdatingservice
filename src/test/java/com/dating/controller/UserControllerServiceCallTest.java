package com.dating.controller;

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
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@WebFluxTest(UserController.class)
@Import(SecurityConfig.class)
class UserControllerServiceCallTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private UserService userService;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User("13252350sgew353tyugh", "love_hurts943@example.com", "love_hurts943",
                null, null, "female", null, "gemini", null);
    }

    @Test
    @DisplayName("GET /userapi/users calls getAllUsers exactly once")
    void listUsersCallsServiceOnce() {
        when(userService.getAllUsers()).thenReturn(Flux.just(user));

        webTestClient.get().uri("/userapi/users")
                .exchange().expectStatus().isOk();

        verify(userService, times(1)).getAllUsers();
    }

    @Test
    @DisplayName("GET /userapi/user/name/{userName} calls findUserByUserName exactly once")
    void userByNameCallsServiceOnce() {
        when(userService.findUserByUserName(user.userName())).thenReturn(Mono.just(user));

        webTestClient.get().uri("/userapi/user/name/{userName}", user.userName())
                .exchange().expectStatus().isOk();

        verify(userService, times(1)).findUserByUserName(user.userName());
    }

    @Test
    @DisplayName("GET /userapi/user/zodiac/{sign} calls getUsersByZodiacSign exactly once")
    void usersByZodiacCallsServiceOnce() {
        when(userService.getUsersByZodiacSign("gemini")).thenReturn(Flux.just(user));

        webTestClient.get().uri("/userapi/user/zodiac/gemini")
                .exchange().expectStatus().isOk();

        verify(userService, times(1)).getUsersByZodiacSign("gemini");
    }

    @Test
    @DisplayName("POST /userapi/addUser rejects malformed email without saving")
    void addUserRejectsMalformedEmail() {
        user = new User(user.id(), "not-an-email", user.userName(), user.profilePictureUrl(),
                user.password(), user.gender(), user.interests(), user.zodiacSign(), user.conversationIds());

        webTestClient.post().uri("/userapi/addUser")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(user)
                .exchange().expectStatus().isBadRequest();

        verify(userService, never()).saveUser(any(User.class));
    }

    @Test
    @DisplayName("POST /userapi/addUser accepts valid email")
    void addUserAcceptsValidEmail() {
        when(userService.saveUser(any(User.class))).thenReturn(Mono.just(user));

        webTestClient.post().uri("/userapi/addUser")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(user)
                .exchange().expectStatus().isCreated();
    }
}
