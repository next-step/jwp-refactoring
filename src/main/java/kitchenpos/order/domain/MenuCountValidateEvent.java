package kitchenpos.order.domain;

import java.util.List;

public class MenuCountValidateEvent {
    private final Order order;

    public MenuCountValidateEvent(Order order) {
        this.order = order;
    }

    public List<Long> getMenuIds() {
        return order.getOrderLineItems().getMenuIds();
    }

    public long getMenuCount() {
        return order.getOrderLineItems().size();
    }
}
