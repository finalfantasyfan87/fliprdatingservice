package com.dating.utility;

import java.util.List;

import com.dating.model.User;
import com.github.javafaker.Faker;

public final class UserDataFactory {
    private static final Faker faker = new Faker();
    private static final List<String> ZODIAC_SIGNS = List.of(
            "Aries", "Taurus", "Gemini", "Cancer", "Leo", "Virgo",
            "Libra", "Scorpio", "Sagittarius", "Capricorn", "Aquarius", "Pisces");

    private UserDataFactory() {
    }

    public static User createDemoUser() {
        return new User(
                faker.numerify("13252350sgew353tyugh"),
                faker.internet().emailAddress(),
                faker.name().username(),
                null,
                faker.gameOfThrones().house(),
                faker.demographic().sex(),
                List.of(
                        faker.book().title(),
                        faker.gameOfThrones().character(),
                        faker.gameOfThrones().quote()),
                ZODIAC_SIGNS.get(faker.random().nextInt(0, ZODIAC_SIGNS.size() - 1)),
                List.of());
    }
}
