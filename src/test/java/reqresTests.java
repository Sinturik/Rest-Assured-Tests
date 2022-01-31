import io.restassured.http.ContentType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

public class reqresTests {
    String url = "https://reqres.in/";
    String dataEmail = "{\n" +
            "    \"email\": \"peter@klaven\"\n" +
            "}";
    String dataForReg = "{\n" +
            "    \"email\": \"eve.holt@reqres.in\",\n" +
            "    \"password\": \"pistol\"\n" +
            "}";
    String dataForUpdate = "{\n" +
            "    \"name\": \"morpheus\",\n" +
            "    \"job\": \"zion resident\"\n" +
            "}";

    @Test
    @DisplayName("Получение списка юзеров")
    void getUsersListTest() {
        given()
                .baseUri(url)
                .basePath("/api/users")
                .contentType(ContentType.JSON)
                .when().get()
                .then().statusCode(200)
                .body("data[0].email", equalTo("george.bluth@reqres.in"))
                .body("data[1].first_name", equalTo("Janet"));
    }

    @Test
    @DisplayName("Получение одного заданного юзера")
    void getSingleUserTest() {
        given()
                .baseUri(url)
                .basePath("/api/users/2")
                .contentType(ContentType.JSON)
                .when().get()
                .then().statusCode(200)
                .body("data.last_name", equalTo("Weaver"));
    }

    @Test
    @DisplayName("Неуспешный логин")
    void unsuccessfulLogin() {
        given()
                .baseUri(url)
                .basePath("/api/login")
                .contentType(ContentType.JSON)
                .body(dataEmail)
                .when().post()
                .then().statusCode(400)
                .body("error", is("Missing password"));
    }

    @Test
    @DisplayName("Успешная регистрация нового юзера")
    void successfulRegistrationTest() {
        given()
                .baseUri(url)
                .basePath("/api/register")
                .contentType(ContentType.JSON)
                .body(dataForReg)
                .when().post()
                .then().statusCode(200)
                .body("id", is(4))
                .body("token", is("QpwL5tke4Pnpja7X4"));
    }

    @Test
    @DisplayName("Удаление юзера")
    void deleteUser() {
        given()
                .baseUri(url)
                .basePath("/api/users/2")
                .contentType(ContentType.JSON)
                .when().delete()
                .then().statusCode(204);
    }

    @Test
    @DisplayName("Обновление юзера")
    void updateUser() {
        given()
                .baseUri(url)
                .basePath("/api/users/2")
                .contentType(ContentType.JSON)
                .body(dataForUpdate)
                .when().put()
                .then().statusCode(200)
                .body("name", is("morpheus"))
                .body("job", is("zion resident"));
    }
}
