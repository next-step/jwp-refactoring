package kitchenpos.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

@Embeddable
public class Orders {

    @OneToMany(mappedBy = "orderTable")
    private List<Order> orders = new ArrayList<>();

    protected Orders() {
    }

    private Orders(List<Order> orders) {
        this.orders = orders;
    }

    public static Orders from(List<Order> orders) {
        return new Orders(orders);
    }

    public List<Order> getOrders() {
        return Collections.unmodifiableList(orders);
    }

    public boolean anyMatchedIn(List<OrderStatus> orderStatuses) {
        return orders.stream().anyMatch(
                it -> orderStatuses.contains(OrderStatus.valueOf(it.getOrderStatus()))
        );
    }
}
