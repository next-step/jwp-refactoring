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

    public static Orders createEmpty() {
        return new Orders(new ArrayList<>());
    }

    public List<Order> getOrders() {
        return Collections.unmodifiableList(orders);
    }

    public boolean anyMatchedIn(List<OrderStatus> orderStatuses) {
        return orders.stream().anyMatch(
                it -> orderStatuses.contains(OrderStatus.valueOf(it.getOrderStatus()))
        );
    }

    public void add(Order order) {
        if (this.orders.contains(order)) {
            return;
        }

        this.orders.add(order);
    }
}
