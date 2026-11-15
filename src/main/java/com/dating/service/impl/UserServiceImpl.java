package com.dating.service.impl;

import com.dating.service.UserService;
import com.dating.model.User;
import com.dating.repository.UserMongoRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Service("userService")
public class UserServiceImpl implements UserService {
    private final UserMongoRepository userMongoRepository;

    public UserServiceImpl(UserMongoRepository userMongoRepository) {
        this.userMongoRepository = userMongoRepository;
    }

    @Override
    public Mono<User> getUserById(String id) {
        return userMongoRepository.findById(id);
    }

    @Override
    public Flux<User> getUsersByZodiacSign(String zodiacSign) {
        return userMongoRepository.findAllByZodiacSign(zodiacSign);
    }

    @Override
    public Flux<User> getAllUsers() {
        return userMongoRepository.findAll();
    }

    @Override
    public Flux<String> getInterestsById(String id) {
        return userMongoRepository.findProjectedInterestsById(id)
                .flatMapMany(user -> Flux.fromIterable(
                        user.interests() == null ? List.of() : user.interests()));
    }

    @Override
    public Mono<User> findUserByUserName(String userName) {
        return userMongoRepository.findUserByUserName(userName);
    }

    @Override
    public Mono<User> saveUser(User user) {
        return userMongoRepository.save(user);
    }
}
