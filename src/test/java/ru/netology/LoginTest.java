package ru.netology;

import com.codeborne.selenide.Condition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.time.Duration;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static ru.netology.DataGenerator.Registration.getRegisteredUser;
import static ru.netology.DataGenerator.Registration.getUser;
import static ru.netology.DataGenerator.getRandomLogin;
import static ru.netology.DataGenerator.getRandomPassword;

public class LoginTest {

    @BeforeEach
    void setup() {
        open("http://localhost:9999");
    }

    @Test
    void successfulLoginIfUserExistsAndIsActive() {
        var registeredUser = getRegisteredUser("active");
        $("[data-test-id='login'] input").setValue(registeredUser.getLogin());
        $("[data-test-id='password'] input").setValue(registeredUser.getPassword());
        $("button.button").click();
        $("h2").should(Condition.exactText("Личный кабинет"), Condition.visible);
    }

    @Test
    void failedLoginIfUserIsNotRegistered() {
        var notRegisteredUser = getUser("active");
        $("[data-test-id='login'] input").setValue(notRegisteredUser.getLogin());
        $("[data-test-id='password'] input").setValue(notRegisteredUser.getPassword());
        $("button.button").click();
        $("[data-test-id='error-notification'] .notification__content").shouldHave(Condition.text("Ошибка! Неверно указан логин или пароль"), Duration.ofSeconds(15)).shouldBe((Condition.visible));
    }

    @Test
    void failedLoginIfUserIsBlocked() {
        var blockedUser = getRegisteredUser("blocked");
        $("[data-test-id='login'] input").setValue(blockedUser.getLogin());
        $("[data-test-id='password'] input").setValue(blockedUser.getPassword());
        $("button.button").click();
        $("[data-test-id='error-notification'] .notification__content").shouldHave(Condition.text("Ошибка! Пользователь заблокирован"), Duration.ofSeconds(15)).shouldBe((Condition.visible));
    }

    @Test
    void failedLoginIfUsernameIsInvalid() {
        var registeredUser = getRegisteredUser("active");
        var wrongLogin =getRandomLogin();
        $("[data-test-id='login'] input").setValue(wrongLogin);
        $("[data-test-id='password'] input").setValue(registeredUser.getPassword());
        $("button.button").click();
        $("[data-test-id='error-notification'] .notification__content").shouldHave(Condition.text("Ошибка! Неверно указан логин или пароль"), Duration.ofSeconds(15)).shouldBe((Condition.visible));
    }

    @Test
    void failedLoginIfPasswordIsInvalid() {
        var registeredUser = getRegisteredUser("active");
        var wrongPassword =getRandomPassword();
        $("[data-test-id='login'] input").setValue(registeredUser.getLogin());
        $("[data-test-id='password'] input").setValue(wrongPassword);
        $("button.button").click();
        $("[data-test-id='error-notification'] .notification__content").shouldHave(Condition.text("Ошибка! Неверно указан логин или пароль"), Duration.ofSeconds(15)).shouldBe((Condition.visible));
    }
}
