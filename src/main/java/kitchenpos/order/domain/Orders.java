package kitchenpos.order.domain;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

@Embeddable
public class Orders {

    @OneToMany(mappedBy = "orderTable", orphanRemoval = true)
    List<Order> orders = new ArrayList<>();

    public Orders() {
    }

    public Orders(List<Order> orders) {
        this.orders = orders;
    }

    public boolean isNotCompleted() {
        return orders.stream()
                .anyMatch(Order::cannotBeChanged);
    }

    public void add(Order order) {
        orders.add(order);
    }
}