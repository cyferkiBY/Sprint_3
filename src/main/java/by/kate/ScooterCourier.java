package by.kate;

import lombok.Builder;
import lombok.Data;
import org.apache.commons.lang3.RandomStringUtils;

@Data
@Builder
public class ScooterCourier {

    private String login;
    private String password;
    private String firstName;

    public ScooterCourier(String login, String password, String firstName) {
        this.login = login;
        this.password = password;
        this.firstName = firstName;
    }

    public static ScooterCourier getRandomScooterCourier() {
        final String courierRandomLogin = RandomStringUtils.randomAlphabetic(10);
        final String courierRandomPassword = RandomStringUtils.randomAlphabetic(10);
        final String courierRandomFirstName = RandomStringUtils.randomAlphabetic(10);

        return new ScooterCourier(courierRandomLogin, courierRandomPassword, courierRandomFirstName);
    }
}