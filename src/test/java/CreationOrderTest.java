import io.qameta.allure.Description;
import io.restassured.RestAssured;
import org.junit.Before;
import org.junit.Test;
import io.qameta.allure.junit4.DisplayName;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;

@RunWith(Parameterized.class)
public class CreationOrderTest {
       private final String[] color;
       private final String colorForTest;

    public CreationOrderTest(String colorForTest, String[] color) {
        this.colorForTest=colorForTest;
        this.color = color;
    }

    @Parameterized.Parameters(name = "Тест {index}: Цвет самоката {0}")
    public static Object[][] getTestData() {


        return new Object[][]{
                {"Чёрный", new String[]{"BLACK"}},
                {"Серый", new String[]{"GREY"}},
                {"Черный и Серый", new String[]{"BLACK", "GREY"}},
                {"Без цвета", null}
        };
    }


    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru/";
        }

    @Test
    @Description("Создание заказа")
    public void creationOfOrder(){
        Order order = new Order("Vadim",
                "Kulikov",
                "Saratov",
                4,
                "89372680801",
                2,
                "2024-06-06",
                "123",
                color);

            given()
                    .header("Content-type", "application/json")
                    .and()
                    .body(order)
                    .when()
                    .post("/api/v1/orders")
                    .then().statusCode(201)
                    .assertThat().body("track", notNullValue());
    }
}