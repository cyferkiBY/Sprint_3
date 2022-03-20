package by.kate;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ScooterCourierLoginTest {

    ScooterClient scooterClient;
    ScooterCourier courierWithValidData;
    ScooterCourierCredentials courierCredentialsWithValidData;
    int courierId;

    @Before
    public void setUp() {
        scooterClient = new ScooterClient();
        courierWithValidData = ScooterCourier.getRandomScooterCourier();
        scooterClient.createCourier(courierWithValidData);
        courierCredentialsWithValidData = new ScooterCourierCredentials(courierWithValidData.getLogin(), courierWithValidData.getPassword());
        courierId = scooterClient.getIdScooterCourier(courierCredentialsWithValidData);
    }

    @Test
    @DisplayName("Check courier can login with valid date")
    public void courierCanLoginWithValidCredentials() {
        ValidatableResponse loginResponse = scooterClient.loginCourier(courierCredentialsWithValidData);
        int statusCode = loginResponse.extract().statusCode();
        if (statusCode == 200) {
            courierId = loginResponse.extract().path("id");
        } else {
            courierId = 0;
        }
        Assert.assertNotEquals("Не удалось залогиниться", 0, courierId);
        Assert.assertEquals("Не верный статус-код", 200, statusCode);
    }

    @Test
    @DisplayName("Check courier can't login with not valid password")
    public void courierCannotLoginWithNotValidPassword() {
        String wrongPassword = "wrongPassword";

        ValidatableResponse loginResponse = scooterClient.loginCourier(new ScooterCourierCredentials(courierWithValidData.getLogin(), wrongPassword));
        int statusCode = loginResponse.extract().statusCode();
        String message = loginResponse.extract().path("message");
        Assert.assertEquals("Не верное сообщение об ошибке", "Учетная запись не найдена", message);
        Assert.assertEquals("Не верный статус-код", 404, statusCode);
    }

    @Test
    @DisplayName("Check courier can't login with not valid login")
    public void courierCannotLoginWithNotValidLogin() {
        String wrongLogin = "wrongLogin";

        ValidatableResponse loginResponse = scooterClient.loginCourier(new ScooterCourierCredentials(wrongLogin, courierWithValidData.getPassword()));
        int statusCode = loginResponse.extract().statusCode();
        String message = loginResponse.extract().path("message");
        Assert.assertEquals("Не верное сообщение об ошибке", "Учетная запись не найдена", message);
        Assert.assertEquals("Не верный статус-код", 404, statusCode);
    }

    @Test
    @DisplayName("Check courier can't login with not valid login and password")
    public void courierCannotLoginWithNotValidLoginAndPassword() {
        String wrongLogin = "wrongLogin";
        String wrongPassword = "wrongPassword";

        ValidatableResponse loginResponse = scooterClient.loginCourier(new ScooterCourierCredentials(wrongLogin, wrongPassword));
        int statusCode = loginResponse.extract().statusCode();
        String message = loginResponse.extract().path("message");
        Assert.assertEquals("Не верное сообщение об ошибке", "Учетная запись не найдена", message);
        Assert.assertEquals("Не верный статус-код", 404, statusCode);
    }

    @Test
    @DisplayName("Check courier can't login without password")
    public void courierCannotLoginWithoutPassword() {
        ValidatableResponse loginResponse = scooterClient.loginCourier(ScooterCourierCredentials.builder().login(courierWithValidData.getLogin()).password("").build());
        int statusCode = loginResponse.extract().statusCode();
        String message = loginResponse.extract().path("message");
        Assert.assertEquals("Не верное сообщение об ошибке", "Недостаточно данных для входа", message);
        Assert.assertEquals("Не верный статус-код", 400, statusCode);
    }

    @Test
    @DisplayName("Check courier can't login without login")
    public void courierCannotLoginWithoutLogin() {
        ValidatableResponse loginResponse = scooterClient.loginCourier(ScooterCourierCredentials.builder().login("").password(courierWithValidData.getPassword()).build());
        int statusCode = loginResponse.extract().statusCode();
        String message = loginResponse.extract().path("message");
        Assert.assertEquals("Не верное сообщение об ошибке", "Недостаточно данных для входа", message);
        Assert.assertEquals("Не верный статус-код", 400, statusCode);
    }

    @After
    public void tearDown() {
        scooterClient.deleteCourier(courierId);
    }
}