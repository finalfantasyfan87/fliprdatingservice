package com.dating.service;

import com.dating.model.User;
import com.dating.repository.UserMongoRepository;
import com.dating.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class UserServiceImplTest {

    private final UserMongoRepository repository = mock(UserMongoRepository.class);
    private final UserServiceImpl service = new UserServiceImpl(repository);

    @Test
    void getInterestsByIdEmitsProjectedInterests() {
        User user = new User("user-1", null, null, null, null, null,
                List.of("hiking", "music"), null, null);
        when(repository.findProjectedInterestsById("user-1")).thenReturn(Mono.just(user));

        StepVerifier.create(service.getInterestsById("user-1"))
                .expectNext("hiking", "music")
                .verifyComplete();
    }

    @Test
    void getInterestsByIdCompletesWhenTheUserHasNoInterests() {
        User user = new User("user-1", null, null, null, null, null, null, null, null);
        when(repository.findProjectedInterestsById("user-1")).thenReturn(Mono.just(user));

        StepVerifier.create(service.getInterestsById("user-1"))
                .verifyComplete();
    }
}
