package com.dating.service;

import com.dating.model.User;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserService {
    Mono<User> getUserById(String id);

    Flux<User> getUsersByZodiacSign(String zodiacSign);

    Flux<User> getAllUsers();

    Flux<String> getInterestsById(String id);

    Mono<User> findUserByUserName(String userName);

    Mono<User> saveUser(User user);
}
