package ru.netology.testmode.test;

import com.codeborne.selenide.Condition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Selenide.*;
import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.requestSpecification;
import static ru.netology.testmode.data.DataGenerator.*;
import static ru.netology.testmode.data.DataGenerator.Registration.getRegisteredUser;
import static ru.netology.testmode.data.DataGenerator.Registration.getUser;

class AuthTest {

    @BeforeEach
    void setup() {
        open("http://localhost:9999");
    }

    @Test
    @DisplayName("Should successfully login with active registered user")
    void shouldSuccessfulLoginIfRegisteredActiveUser() {
        var registeredUser = getRegisteredUser("active");
        $("[name=login]").setValue(registeredUser.getLogin());
        $("[name=password]").setValue(registeredUser.getPassword());
        $(".button__text").click();
        $x("//*[contains(text(), 'Личный кабинет')]").shouldBe(Condition.visible);

    }

    @Test
    @DisplayName("Should get error message if login with not registered user")
    void shouldGetErrorIfNotRegisteredUser() {
        $("[name=login]").setValue("123456");
        $("[name=password]").setValue("Ekaterina");
        $(".button__text").click();
        $(".notification__title").shouldBe(Condition.visible);
    }

    @Test
    @DisplayName("Should get error message if login with blocked registered user")
    void shouldGetErrorIfBlockedUser() {
        var blockedUser = getRegisteredUser("blocked");
        $("[name=login]").setValue(blockedUser.getLogin());
        $("[name=password]").setValue(blockedUser.getPassword());
        $(".button__text").click();
        $(".notification__title").shouldBe(Condition.visible);
    }

    @Test
    @DisplayName("Should get error message if login with wrong login")
    void shouldGetErrorIfWrongLogin() {
        var registeredUser = getRegisteredUser("active");
        var wrongLogin = getInvalidLogin();
        $("[name=login]").setValue(wrongLogin);
        $("[name=password]").setValue(registeredUser.getPassword());
        $(".button__text").click();
        $(".notification__title").shouldBe(Condition.visible);

    }

    @Test
    @DisplayName("Should get error message if login with wrong password")
    void shouldGetErrorIfWrongPassword() {
        var registeredUser = getRegisteredUser("active");
        var wrongPassword = getRandomInvalidPassword();
        $("[name=login]").setValue(registeredUser.getLogin());
        $("[name=password]").setValue(wrongPassword);
        $(".button__text").click();
        $(".notification__title").shouldBe(Condition.visible);
    }
}
