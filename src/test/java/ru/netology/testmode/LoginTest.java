package ru.netology.testmode;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;
import static ru.netology.data.DataGenerator.Registration.getRegisteredUser;
import static ru.netology.data.DataGenerator.Registration.getUser;
import static ru.netology.data.DataGenerator.getRandomLogin;
import static ru.netology.data.DataGenerator.getRandomPassword;


public class LoginTest {

    @BeforeEach
    void setup() {
        open("http://localhost:7777");
    }

    @Test
    @DisplayName("Should successfully login, registered user")
    void shouldSuccessfulLoginIfRegisteredValidUser() {
        var registeredUser = getRegisteredUser("active");
        $("[data-test-id= login]").$("[class= input__control]").setValue(registeredUser.getLogin());
        $("[data-test-id= password]").$("[class= input__control]").setValue(registeredUser.getPassword());
        $$("button").find(exactText("Продолжить")).click();
        $("[id = root]").shouldHave(exactText("Личный кабинет"));
    }

    @Test
    @DisplayName("Should show warning if not registered user")
    void shouldGetErrorIfNotRegisteredUser() {
        var notRegisteredUser = getUser("active");
        $("[data-test-id= login]").$("[class= input__control]").setValue(notRegisteredUser.getLogin());
        $("[data-test-id= password]").$("[class= input__control]").setValue(notRegisteredUser.getPassword());
        $$("button").find(exactText("Продолжить")).click();
        $("[data-test-id= error-notification]").shouldBe(visible).
                shouldHave(exactText("Ошибка\n" + "Ошибка! Неверно указан логин или пароль"));
    }

    @Test
    @DisplayName("Should show warning if blocked registered user")
    void shouldGetErrorIfBlockedUser() {
        var blockedUser = getRegisteredUser("blocked");
        $("[data-test-id= login]").$("[class= input__control]").setValue(blockedUser.getLogin());
        $("[data-test-id= password]").$("[class= input__control]").setValue(blockedUser.getPassword());
        $$("button").find(exactText("Продолжить")).click();
        $("[data-test-id= error-notification]").shouldBe(visible).
                shouldHave(exactText("Ошибка\n" + "Ошибка! Пользователь заблокирован"));
    }

    @Test
    @DisplayName("Should show warning for the wrong login")
    void shouldGetErrorIfWrongLogin() {
        var registeredUser = getRegisteredUser("active");
        var wrongLogin = getRandomLogin();
        $("[data-test-id= login]").$("[class= input__control]").setValue(registeredUser.getLogin());
        $("[data-test-id= password]").$("[class= input__control]").setValue(wrongLogin);
        $$("button").find(exactText("Продолжить")).click();
        $("[data-test-id= error-notification]").shouldBe(visible).
                shouldHave(exactText("Ошибка\n" + "Ошибка! Неверно указан логин или пароль"));

    }

    @Test
    @DisplayName("Should show warning for the wrong password")
    void shouldGetErrorIfWrongPassword() {
        var registeredUser = getRegisteredUser("active");
        var wrongPassword = getRandomPassword();
        $("[data-test-id= login]").$("[class= input__control]").setValue(registeredUser.getLogin());
        $("[data-test-id= password]").$("[class= input__control]").setValue(wrongPassword);
        $$("button").find(exactText("Продолжить")).click();
        $("[data-test-id= error-notification]").shouldBe(visible).
                shouldHave(exactText("Ошибка\n" + "Ошибка! Неверно указан логин или пароль"));
    }
}
