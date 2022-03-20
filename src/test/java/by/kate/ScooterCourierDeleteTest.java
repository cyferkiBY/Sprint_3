package by.kate;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ScooterCourierDeleteTest {
    ScooterClient courierClient;
    int courierId;

    @Before
    public void setUp() {
        courierClient = new ScooterClient();
    }

    @Test
    @DisplayName("Check delete courier with valid id")
    public void checkDeleteCourierWithValidId() {
        courierId = courierClient.createRandomCourier();
        ValidatableResponse deleteResponse = courierClient.deleteCourier(courierId);
        int statusCode = deleteResponse.extract().statusCode();
        boolean message = deleteResponse.extract().path("ok");
        Assert.assertEquals("Не верный статус-код", 200, statusCode);
        Assert.assertTrue("Не удалось удалить курьера", message);
    }

    @Test
    @DisplayName("Check delete courier with not valid id")
    public void checkDeleteCourierWithNotValidId() {
        //для того чтобы убедиться что ID не существует, попробуем удалить курьера которого только что удалили
        courierId = courierClient.createRandomCourier();
        courierClient.deleteCourier(courierId);
        ValidatableResponse deleteResponse = courierClient.deleteCourier(courierId);
        int statusCode = deleteResponse.extract().statusCode();
        String message = deleteResponse.extract().path("message");
        Assert.assertEquals("Не верный статус-код", 404, statusCode);
        Assert.assertEquals("Не верное сообщение об ошибке", "Курьера с таким id нет", message);
    }

    @Test
    @DisplayName("Check delete courier without id")
    public void checkDeleteCourierWithoutId() {
        ValidatableResponse deleteResponse = courierClient.deleteCourier(0);
        int statusCode = deleteResponse.extract().statusCode();
        String message = deleteResponse.extract().path("message");
        Assert.assertEquals("Не верный статус-код", 400, statusCode);
        Assert.assertEquals("Не верное сообщение об ошибке", "Недостаточно данных для удаления курьера", message);
    }
}