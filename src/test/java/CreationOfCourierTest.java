import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.response.Response;
import org.junit.*;
import io.qameta.allure.junit4.DisplayName;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class CreationOfCourierTest {
    CourierOperations courier =
            new CourierOperations(Constants.LOGIN_FOR_CREATION, Constants.PASSWORD_FOR_CREATION, Constants.FIRST_NAME);

    @Before
    public void setUp() {
        RestAssured.baseURI = Constants.URL;
//        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
    }

    @Step("Создание курьера")
    public Response creationOfCourier() {
        return
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(courier)
                        .when()
                        .post("/api/v1/courier");

    }
    @Step("получение id курьера")
    public static int pickIdOfCourier() {
        CourierOperations courier =
                new CourierOperations(Constants.LOGIN_FOR_CREATION,
                        Constants.PASSWORD_FOR_CREATION,
                        Constants.FIRST_NAME);

        CourierId id = given()
                .header("Content-type", "application/json")
                .and()
                .body(courier)
                .when()
                .post("/api/v1/courier/login").as(CourierId.class);
        return id.getId();
    }

    @Test
    @DisplayName("Проверка на успешное создание курьера и создание дубликата")
    public void successfullyCreationOfCourier() {
        creationOfCourier().then().statusCode(201)
                .assertThat().body("ok", equalTo(true));
        creationOfCourier().then().statusCode(409)
                .assertThat()
                .body("message", equalTo("Этот логин уже используется. Попробуйте другой."));
    }

    @Test
    @DisplayName("Проверка на создание курьера без логина")
    public void checkCreationOfCourierWithoutLogin() {
        courier.setLogin(null);
        creationOfCourier().then().statusCode(400)
                .assertThat()
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("Проверка на создание курьера без пароля")
    public void checkCreationOfCourierWithoutPassword() {
        courier.setPassword(null);
        creationOfCourier().then().statusCode(400)
                .assertThat()
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @After
    public void deleteCourier() {
//        RestAssured.reset();
                given().delete("/api/v1/courier/"+pickIdOfCourier());

    }
}

