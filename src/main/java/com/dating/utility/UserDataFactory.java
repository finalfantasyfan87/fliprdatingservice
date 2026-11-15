package com.dating.utility;

import com.dating.model.User;
import com.github.javafaker.Faker;

import java.util.List;

public final class UserDataFactory {
    private static final Faker FAKER = new Faker();
    private static final List<String> ZODIAC_SIGNS = List.of(
            "Aries", "Taurus", "Gemini", "Cancer", "Leo", "Virgo",
            "Libra", "Scorpio", "Sagittarius", "Capricorn", "Aquarius", "Pisces");

    private UserDataFactory() {
    }

    public static User createDemoUser() {
        return new User(
                FAKER.numerify("13252350sgew353tyugh"),
                FAKER.internet().emailAddress(),
                FAKER.name().username(),
                null,
                FAKER.gameOfThrones().house(),
                FAKER.demographic().sex(),
                List.of(
                        FAKER.book().title(),
                        FAKER.gameOfThrones().character(),
                        FAKER.gameOfThrones().quote()),
                ZODIAC_SIGNS.get(FAKER.random().nextInt(0, ZODIAC_SIGNS.size() - 1)),
                List.of());
    }
}
