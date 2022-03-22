package by.kate;

import lombok.Data;

@Data
public class ScooterOrderWithTrack {
    private ScooterOrder order;

    public ScooterOrderWithTrack(ScooterOrder order) {
        this.order = order;
    }
}