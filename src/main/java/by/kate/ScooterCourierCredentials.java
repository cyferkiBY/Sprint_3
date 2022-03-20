package by.kate;

import io.qameta.allure.internal.shadowed.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ScooterCourierCredentials {

    private final String login;
    private final String password;

    public ScooterCourierCredentials(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public ScooterCourierCredentials(ScooterCourier courier) {
        this.login = courier.getLogin();
        this.password = courier.getPassword();
    }
}