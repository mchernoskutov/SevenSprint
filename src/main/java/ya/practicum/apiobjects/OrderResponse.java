package ya.practicum.apiobjects;

import java.util.List;

public class OrderResponse {

    private List<Orders> orders;
    private PageInfo pageInfo;
    private List<AvailableStation> availableStations;

    public OrderResponse() {

    }

    public OrderResponse(List<Orders> orders) {
        this.orders = orders;
    }


    public List<Orders> getOrder() {
        return orders;
    }

    public void setOrder(List<Orders> orders) {
        this.orders = orders;
    }

    public PageInfo getPageInfo() {
        return pageInfo;
    }

    public void setPageInfo(PageInfo pageInfo) {
        this.pageInfo = pageInfo;
    }

    public List<AvailableStation> getAvailableStations() {
        return availableStations;
    }

    public void setAvailableStations(List<AvailableStation> availableStations) {
        this.availableStations = availableStations;
    }
}
