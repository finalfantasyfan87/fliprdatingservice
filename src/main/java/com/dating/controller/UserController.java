package com.dating.controller;

import java.net.URI;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.dating.model.User;
import com.dating.service.UserService;
import com.dating.utility.ValidationUtil;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/userapi")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    public Flux<User> getUsers() {
        return userService.getAllUsers()
                .switchIfEmpty(Flux.error(
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "No users were found")));
    }

    @GetMapping("/user/{id}")
    public Mono<User> getUserById(@PathVariable String id) {
        return userService.getUserById(id)
                .switchIfEmpty(Mono.error(
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "User was not found")));
    }

    @GetMapping("/user/name/{userName}")
    public Mono<User> getUserByUserName(@PathVariable String userName) {
        return userService.findUserByUserName(userName)
                .switchIfEmpty(Mono.error(
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "User was not found")));
    }

    @GetMapping("/user/zodiac/{zodiacSign}")
    public Flux<User> getUsersByZodiacSign(@PathVariable String zodiacSign) {
        return userService.getUsersByZodiacSign(zodiacSign)
                .switchIfEmpty(Flux.error(new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "No users were found for that zodiac sign")));
    }

    @PostMapping("/addUser")
    public Mono<ResponseEntity<User>> addUser(@RequestBody Mono<User> user) {
        return user.flatMap(userToSave -> {
            if (!ValidationUtil.isEmailValid(userToSave.email())) {
                return Mono.error(new IllegalArgumentException("A valid email address is required"));
            }
            return userService.saveUser(userToSave)
                    .map(savedUser -> ResponseEntity.created(URI.create("/addUser/" + savedUser.id()))
                            .body(savedUser));
        });
    }

    @GetMapping("/test")
    public Mono<String> test() {
        return Mono.just("API is working!");
    }
}
