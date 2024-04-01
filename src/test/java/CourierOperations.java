import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Assert;
import org.junit.Test;

import static io.restassured.RestAssured.given;

public class CourierOperations {
    private String login;
    private String password;

    private String firstName;

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }



    public CourierOperations(String login, String password, String firstName){
        this.login=login;
        this.password=password;
        this.firstName=firstName;
    }

    public CourierOperations(String login, String firstName){
        this.login=login;
        this.firstName=firstName;
    }

    public CourierOperations(){

    }

}
