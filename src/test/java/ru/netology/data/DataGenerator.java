package ru.netology.data;

import lombok.Data;
import lombok.Value;

import java.util.Locale;

import static io.restassured.RestAssured.given;

import com.github.javafaker.Faker;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

@Data

public class DataGenerator {
    private static final RequestSpecification requestSpec = new RequestSpecBuilder()
            .setBaseUri("http://localhost")
            .setPort(7777)
            .setAccept(ContentType.JSON)
            .setContentType(ContentType.JSON)
            .log(LogDetail.ALL)
            .build();

    private static final Faker faker = new Faker(new Locale("en"));

    private DataGenerator() {
    }

    private static void sendRequest(RegistrationDto user) {
        given()
                .spec(requestSpec)
                .body(new RegistrationDto(user.getLogin(), user.getPassword(), user.getStatus()))
                .when()
                .post("/api/system/users");
    }

    public static String getRandomLogin() {
        String login = faker.animal().name();
        ;
        return login;
    }

    public static String getRandomPassword() {
        String password = faker.bothify("?#???#?#");
        return password;
    }

    public static class Registration {
        private Registration() {
        }

        public static RegistrationDto getUser(String status) {
            String login = getRandomLogin();
            String password = getRandomPassword();
            var user = new RegistrationDto(login, password, status);
            return user;
        }

        public static RegistrationDto getRegisteredUser(String status) {
            var registeredUser = getUser(status);
            sendRequest(registeredUser);
            return registeredUser;
        }
    }

    @Value
    public static class RegistrationDto {
        String login;
        String password;
        String status;
    }
}
