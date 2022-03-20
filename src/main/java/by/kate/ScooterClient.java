package by.kate;

import io.qameta.allure.Step;
import io.qameta.allure.internal.shadowed.jackson.annotation.JsonInclude;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;

import java.util.HashMap;

import static io.restassured.RestAssured.given;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ScooterClient extends ScooterRestClient {

    private static final String COURIER_PATH = "api/v1/courier/";
    private static final String ORDERS_PATH = "api/v1/orders/";

    @Step("Login courier")
    public ValidatableResponse loginCourier(ScooterCourierCredentials credential) {
        return given()
                .spec(getBaseSpec())
                .body(credential)
                .when()
                .post(COURIER_PATH + "login")
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
        String id;
        if (idScooterCourier == 0) {
            id = "";
        } else {
            id = String.valueOf(idScooterCourier);
        }
        return given()
                .spec(getBaseSpec())
                .delete(COURIER_PATH + id)
                .then();
    }

    @Step("Get ID courier")
    public int getIdScooterCourier(ScooterCourierCredentials credential) {
        ValidatableResponse response = loginCourier(credential);
        if (response.extract().statusCode() == 200) {
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

    @Step("Get order by track")
    public Response getOrderByTrack(int idTrack) {
        String track;
        if (idTrack == 0) {
            track = "";
        } else {
            track = String.valueOf(idTrack);
        }
        return given()
                .spec(getBaseSpec())
                .queryParam("t", track)
                .when()
                .get(ORDERS_PATH + "track");
    }

    @Step("Get order ID by track")
    public int getOrderIdByTrack(int idTrack) {
        String track;
        if (idTrack == 0) {
            track = "";
        } else {
            track = String.valueOf(idTrack);
        }
        return given()
                .spec(getBaseSpec())
                .queryParam("t", track)
                .when()
                .get(ORDERS_PATH + "track").then().extract().path("order.id");
    }

    @Step("Put accept order by track")
    public ValidatableResponse putAcceptOrder(int orderId, int courierId) {
        String idOrderString;
        String idCourierString;
        if (orderId == 0) {
            idOrderString = "";
        } else {
            idOrderString = String.valueOf(orderId);
        }
        if (courierId == 0) {
            idCourierString = "";
        } else {
            idCourierString = String.valueOf(courierId);
        }
        return given()
                .spec(getBaseSpec())
                .queryParam("courierId", idCourierString)
                .log().all()
                .when()
                .put(ORDERS_PATH + "accept/" + idOrderString)
                .then()
                .log().all();
    }

    @Step("Create random courier")
    public int createRandomCourier() {
        ScooterCourier courierWithValidData = ScooterCourier.getRandomScooterCourier();
        ScooterCourierCredentials courierCredentialsWithValidData = new ScooterCourierCredentials(courierWithValidData);
        createCourier(courierWithValidData);
        return getIdScooterCourier(courierCredentialsWithValidData);
    }

    @Step("Create random order")
    public HashMap<String, Integer> createRandomOrder() {
        HashMap<String, Integer> TrackAndIdOrder = new HashMap<>();
        ScooterClient scooterClient = new ScooterClient();
        ScooterOrder orderWithValidData = ScooterOrder.getRandomScooterOrder();
        ValidatableResponse createResponse = scooterClient.createOrder(orderWithValidData);
        int statusCode = createResponse.extract().statusCode();
        if (statusCode == 201) {
            int track = createResponse.extract().path("track");
            TrackAndIdOrder.put("track", track);
            TrackAndIdOrder.put("id", getOrderIdByTrack(track));

        } else {
            TrackAndIdOrder.put("track", 0);
            TrackAndIdOrder.put("id", 0);
        }
        return TrackAndIdOrder;
    }

    @Step("Cancel order by track {track}")
    public ValidatableResponse cancelOrderByTrack(int track) {
        return given()
                .spec(getBaseSpec())
                .body("{\"track\": " + track + "}")
                .put(COURIER_PATH + "cancel")
                .then();
    }
}