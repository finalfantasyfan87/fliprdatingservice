package com.dating.repository;

import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.dating.model.User;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserMongoRepository extends ReactiveMongoRepository<User, String> {

    Flux<User> findAllByZodiacSign(String zodiacSign);

    Mono<User> findUserByUserName(String userName);

    @Query(value = "{ '_id': ?0 }", fields = "{ 'interests': 1 }")
    Mono<User> findProjectedInterestsById(String id);
}
