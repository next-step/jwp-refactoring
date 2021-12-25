package kitchenpos.order.domain;

import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Embeddable
public class Orders {
    @OneToMany(mappedBy = "orderTable")
    private List<Order> orders = new ArrayList<>();

    public boolean canUngroupOrChange() {
        return orders.stream()
                .map(Order::getOrderStatus)
                .anyMatch(orderStatus -> orderStatus == OrderStatus.COOKING || orderStatus == OrderStatus.MEAL);
    }

    public void add(Order order) {
        this.orders.add(order);
    }
}
