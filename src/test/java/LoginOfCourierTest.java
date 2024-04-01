import io.qameta.allure.Description;
import io.qameta.allure.Issue;
import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import io.qameta.allure.junit4.DisplayName;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class LoginOfCourierTest {
        LoginOfCourier login =
            new LoginOfCourier(Constants.LOGIN_FOR_ENTERING, Constants.PASSWORD_FOR_ENTERING);

    @Before
    public void setUp() {
        RestAssured.baseURI = Constants.URL;
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
    }

    @Step("Шаг - логин курьера в систему")
    public Response loginIntoSystem() {
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(login)
                        .when()
                        .post("/api/v1/courier/login");
        return response;
    }

    @Step
    @DisplayName("Получение id курьера")
    public void idOfCourier() {
        CourierId courierId = loginIntoSystem().as(CourierId.class);
    }

    @Test
    @DisplayName("Успешный логин")
    public void successfullyLoginCheck() {
        loginIntoSystem().then().statusCode(200).
                assertThat().body("id", notNullValue());
    }


    @Test
    @DisplayName("Проверка входа в систему без логина")
    public void checkEnteringWithoutLogin() {
        login.setLogin(null);
        loginIntoSystem()
                .then().statusCode(400)
                .assertThat().body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("Проверка входа в систему без пароля")
    @Issue("По документации ожидается ошибка 400, а приходит 504")
    public void checkEnteringWithoutPassword() {
        login.setPassword(null);
        loginIntoSystem()
                .then().statusCode(400)
                .assertThat().body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("Проверка входа в систему несуществующего пользователя")
    public void checkIncorrectLogin() {
        login.setLogin(Constants.NOT_EXISTING_LOGIN);
        login.setPassword(Constants.NOT_EXISTING_PASSWORD);
        loginIntoSystem()
                .then().statusCode(404)
                .assertThat().body("message", equalTo("Учетная запись не найдена"));
    }

    @After
    public void after(){
        RestAssured.reset();
    }

}

