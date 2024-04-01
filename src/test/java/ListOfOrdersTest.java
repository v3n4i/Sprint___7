import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class ListOfOrdersTest {

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru/";
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
    }

    @Step("получение id курьера")
    public static int pickIdOfCourier() {
        CourierOperations courier =
                new CourierOperations(Constants.LOGIN_FOR_ENTERING,
                        Constants.PASSWORD_FOR_ENTERING,
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
    @DisplayName("Проверка списка заказов по id курьера")
    public void checkOrderListWithFilledCourierId(){
        OrderList orderList = given().header("Content-type", "application/json")
                .get("/api/v1/orders?courierId="+pickIdOfCourier()).body().as(OrderList.class);
        int valueOfOrders = orderList.getPageInfo().getTotal();
        Assert.assertTrue(valueOfOrders>0);
    }

//    @Test
//    public void checkOrderListWithFilledMetroStations(){
//        Gson gson = new GsonBuilder().setPrettyPrinting().create();
//        Response response =
//                given().header("Content-type", "application/json")
//                        .and()
//                        .get("/api/v1/orders?courierId=1&nearestStation=[\"1\", \"2\"]");
//        response.then().statusCode(200)
//                .assertThat().body("orders", notNullValue());
//    }

//    @After
//    public void after(){
//       RestAssured.reset();
//        given().delete("/api/v1/courier/"+pickIdOfCourier());
//
//    }

}
