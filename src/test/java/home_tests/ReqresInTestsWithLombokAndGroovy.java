package home_tests;

import api.models.newmodels.lombok.ListUser;
import api.models.newmodels.lombok.LombokUserData;
import api.models.newmodels.lombok.User;
import io.qameta.allure.Story;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static base.Specs.*;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.hasItem;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class ReqresInTestsWithLombokAndGroovy {

    @Test
    @Story("with lombok")
    void listUsersTest() {
        String path = "/users/8",
                expectedEmail = "lindsay.ferguson@reqres.in",
                expectedName = "Lindsay",
                expectedLastName = "Ferguson",
                expectedUserAvatar = "https://reqres.in/img/faces/8-image.jpg";
        int listUserId = Integer.valueOf(8);
        ListUser data = given()
                .spec(requestSpec)
                .when()
                .get(path)
                .then()
                .spec(responseSpec)
                .log().body()
                .extract().as(ListUser.class);

        assertEquals(listUserId, data.getListData().getIdList());
        assertEquals(expectedEmail, data.getListData().getEmailList());
        assertEquals(expectedName, data.getListData().getFirstNameList());
        assertEquals(expectedLastName, data.getListData().getLastNameList());
        assertEquals(expectedUserAvatar, data.getListData().getAvatarList());
    }

    @Test
    @Story("with groovy")
    void listUsersWithGroovyTest() {
        String path = "/users?page=2",
                expectedEmail = "lindsay.ferguson@reqres.in";
        given()
                .spec(requestSpec)
                .when()
                .get(path)
                .then()
                .log().body()
                .body("data.findAll{it.email =~/.*?@reqres.in/}.email.flatten()",
                        hasItem(expectedEmail));
    }

    @Test
    void singleUserTest() {
        String path = "/users/2";
        given()
                .spec(requestSpec)
                .when()
                .get(path)
                .then()
                .spec(responseSpec)
                .log().body();
    }

    @Test
    void singleUserWithModel() {
        String path = "/users/2";
        LombokUserData data = given()
                .spec(requestSpec)
                .when()
                .get(path)
                .then()
                .spec(responseSpec)
                .log().body()
                .extract().as(LombokUserData.class);
        assertEquals("2", data.getUser().getId());
    }

    @Test
    void singleUserNotFoundTest() {
        String path = "/users/23";
        given()
                .spec(requestSpec)
                .when()
                .get(path)
                .then()
                .spec(responseSpecForNotFound)
                .log().body();
    }

    @Test
    void createTest() {
        String name = "morpheus";
        String job = "leader";
        String path = "/users";
        HashMap bodyParams = new HashMap();
        bodyParams.put("name", name);
        bodyParams.put("job", job);
        User data = given()
                .spec(requestSpec)
                .body(bodyParams)
                .when()
                .post(path)
                .then()
                .spec(responseSpecForCreate)
                .log().body()
                .extract().as(User.class);

        assertNotEquals(null, data.getId());
        assertEquals(name, data.getName());
        assertEquals(job, data.getJob());
        assertNotEquals(null, data.getCreatedAt());
    }

    @Test
    void updateTest() {
        String name = "morpheus";
        String job = "zion resident test new man";
        String path = "/users/2";
        HashMap bodyParams = new HashMap();
        bodyParams.put("name", name);
        bodyParams.put("job", job);
        User data = given()
                .spec(requestSpec)
                .body(bodyParams)
                .when()
                .put(path)
                .then()
                .spec(responseSpec)
                .log().body()
                .extract().as(User.class);
        assertEquals(name, data.getName());
        assertEquals(job, data.getJob());
    }
}
