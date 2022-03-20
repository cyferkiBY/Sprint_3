package by.kate;

import io.qameta.allure.Step;
import io.qameta.allure.internal.shadowed.jackson.annotation.JsonInclude;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ScooterClient extends ScooterRestClient{

    private static final String COURIER_PATH = "api/v1/courier/";
    private static final String ORDERS_PATH = "api/v1/orders/";

    @Step("Login courier")
    public ValidatableResponse loginCourier(ScooterCourierCredentials credential) {
        return given()
                .spec(getBaseSpec())
                .body(credential)
                .when()
                .post(COURIER_PATH+"login")
                .then();
    }

    @Step("Create courier")
    public ValidatableResponse createCourier(ScooterCourier courier) {
        return given()
                .spec(getBaseSpec())
                .body(courier)
                .when()
                .post(COURIER_PATH)
                .then();
    }

    @Step("Delete courier with id {idScooterCourier}")
    public ValidatableResponse deleteCourier(int idScooterCourier) {
        return given()
                .spec(getBaseSpec())
                .delete(COURIER_PATH+idScooterCourier)
                .then();
    }

    @Step("Get ID courier")
    public int getIdScooterCourier(ScooterCourierCredentials credential) {
        ValidatableResponse response = loginCourier(credential);
        if (response.extract().statusCode() == 200){
            return response.extract().path("id");
        } else {
            return 0;
        }
    }

    @Step("Create order")
    public ValidatableResponse createOrder(ScooterOrder order) {
        return given()
                .spec(getBaseSpec())
                .body(order)
                .when()
                .post(ORDERS_PATH)
                .then();
    }

    @Step("Get orders")
    public Response getOrders() {
        return given()
                .spec(getBaseSpec())
                .when()
                .get(ORDERS_PATH);
    }

    @Step("Get orders list")
    public ScooterOrders getListOrders() {
        return getOrders()
                .body()
                .as(ScooterOrders.class);
    }

}
