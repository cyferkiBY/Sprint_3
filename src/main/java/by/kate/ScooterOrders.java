package by.kate;

import lombok.Data;

import java.util.List;

@Data
public class ScooterOrders {

    private List<ScooterOrder> orders;
    private PageInfo pageInfo;
    private List<AvailableStation> availableStations;

    public ScooterOrders(List<ScooterOrder> orders, PageInfo pageInfo, List<AvailableStation> availableStations) {
        this.orders = orders;
        this.pageInfo = pageInfo;
        this.availableStations = availableStations;
    }
}


