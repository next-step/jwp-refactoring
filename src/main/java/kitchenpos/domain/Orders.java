package kitchenpos.domain;

import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Embeddable
public class Orders {
    @OneToMany(mappedBy = "orderTable")
    private List<Order> orders = new ArrayList<>();

    public boolean canUngroup() {
        return orders.stream()
                .map(order -> order.getOrderStatus())
                .anyMatch(orderStatus -> orderStatus == OrderStatus.COOKING || orderStatus == OrderStatus.MEAL);
    }
}
