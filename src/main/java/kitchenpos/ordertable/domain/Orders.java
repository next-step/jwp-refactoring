package kitchenpos.ordertable.domain;

import kitchenpos.order.domain.Order;

import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Embeddable
public class Orders {

    @OneToMany(mappedBy = "orderTable")
    private List<Order> orders = new ArrayList<>();

    protected Orders() {
    }

    public Orders(final List<Order> orders) {
        this.orders = orders;
    }

    public boolean hasNotComplete() {
        return orders.stream()
                .anyMatch(Order::isNotComplete);
    }

    public void add(final Order order) {
        if (orders.contains(order)) {
            throw new IllegalStateException();
        }
        orders.add(order);
    }
}
