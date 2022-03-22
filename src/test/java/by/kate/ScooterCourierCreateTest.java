package by.kate;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ScooterCourierCreateTest {

    ScooterClient courierClient;
    ScooterCourier courierWithValidData;
    int courierId;

    @Before
    public void setUp() {
        courierClient = new ScooterClient();
    }

    @Test
    @DisplayName("Check new courier can create with valid date")
    public void newCourierCanCreateWithValidData() {
        courierWithValidData = ScooterCourier.getRandomScooterCourier();
        ScooterCourierCredentials courierCredentialsWithValidData = new ScooterCourierCredentials(courierWithValidData);

        ValidatableResponse createResponse = courierClient.createCourier(courierWithValidData);
        courierId = courierClient.getIdScooterCourier(courierCredentialsWithValidData);
        int statusCode = createResponse.extract().statusCode();
        boolean message = createResponse.extract().path("ok");

        Assert.assertNotEquals("Не удалось создать курьера", 0, courierId);
        Assert.assertTrue("Не верное сообщение об ошибке", message);
        Assert.assertEquals("Не верный статус-код", 201, statusCode);
    }

    @Test
    @DisplayName("Check new courier can't create without password")
    public void newCourierCannotCreateWithoutPassword() {
        ScooterCourier courierWithoutPassword = ScooterCourier.builder()
                .login("ValidTestLogin")
                .firstName("ValidName")
                .build();
        ValidatableResponse createResponse = courierClient.createCourier(courierWithoutPassword);
        int statusCode = createResponse.extract().statusCode();
        String message = createResponse.extract().path("message");
        Assert.assertEquals("Не верное сообщение об ошибке", "Недостаточно данных для создания учетной записи", message);
        Assert.assertEquals("Не верный статус-код", 400, statusCode);
    }

    @Test
    @DisplayName("Check new courier can't create without login")
    public void newCourierCannotCreateWithoutLogin() {
        ScooterCourier courierWithoutLogin = ScooterCourier.builder()
                .password("ValidPassword")
                .firstName("ValidName")
                .build();
        ValidatableResponse createResponse = courierClient.createCourier(courierWithoutLogin);
        int statusCode = createResponse.extract().statusCode();
        String message = createResponse.extract().path("message");
        Assert.assertEquals("Не верное сообщение об ошибке", "Недостаточно данных для создания учетной записи", message);
        Assert.assertEquals("Не верный статус-код", 400, statusCode);
    }

    @Test
    @DisplayName("Check new courier can create without first name")
    public void newCourierCanCreateWithoutFirstName() {
        ScooterCourier courierWithoutFirstName = ScooterCourier.builder()
                .login("ValidTestLogin")
                .password("ValidPassword")
                .build();
        ScooterCourierCredentials courierCredentialsWithoutFirstName = new ScooterCourierCredentials(courierWithoutFirstName);

        ValidatableResponse createResponse = courierClient.createCourier(courierWithoutFirstName);
        courierId = courierClient.getIdScooterCourier(courierCredentialsWithoutFirstName);
        int statusCode = createResponse.extract().statusCode();
        boolean message = createResponse.extract().path("ok");
        Assert.assertNotEquals("Не удалось создать курьера", 0, courierId);
        Assert.assertTrue("Не верное сообщение об ошибке", message);
        Assert.assertEquals("Не верный статус-код", 201, statusCode);
    }

    @Test
    @DisplayName("Check  can't create two identical courier")
    public void cannotCreateTwoIdenticalCouriers() {
        courierWithValidData = ScooterCourier.getRandomScooterCourier();

        ValidatableResponse createFirstCourierResponse = courierClient.createCourier(courierWithValidData);
        ValidatableResponse createSecondCourierResponse = courierClient.createCourier(courierWithValidData);
        int statusCode = createSecondCourierResponse.extract().statusCode();
        String message = createSecondCourierResponse.extract().path("message");

        Assert.assertEquals("Не верное сообщение об ошибке", "Этот логин уже используется", message);
        Assert.assertEquals("Не верный статус-код", 409, statusCode);
    }

    @Test
    @DisplayName("Check new courier can't create with repeated login")
    public void cannotCreateCouriersWithRepeatedLogin() {
        courierWithValidData = ScooterCourier.getRandomScooterCourier();
        ScooterCourier courierWithRepeatedLogin = new ScooterCourier(courierWithValidData.getLogin(), "newPassword", "Name");

        ValidatableResponse createFirstCourierResponse = courierClient.createCourier(courierWithValidData);
        ValidatableResponse createSecondCourierResponse = courierClient.createCourier(courierWithRepeatedLogin);
        int statusCode = createSecondCourierResponse.extract().statusCode();
        String message = createSecondCourierResponse.extract().path("message");

        Assert.assertEquals("Не верное сообщение об ошибке", "Этот логин уже используется", message);
        Assert.assertEquals("Не верный статус-код", 409, statusCode);
    }

    @After
    public void tearDown() {
        courierClient.deleteCourier(courierId);
    }
}