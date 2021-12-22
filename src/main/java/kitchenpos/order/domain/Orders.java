package kitchenpos.order.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

@Embeddable
public class Orders {
    @OneToMany(mappedBy = "orderTable")
    private List<Order> orders = new ArrayList<>();

    protected Orders() {
    }

    public Orders(List<Order> orders) {
        this.orders = orders;
    }

    public List<Order> getValue() {
        return orders;
    }

    public void add(Order order) {
        if (!orders.contains(order)) {
            orders.add(order);
        }
    }

    public void validateOrderStatusForUngroup() {
        for (Order order : orders) {
            order.validateOrderStatusForUngroup();
        }
    }
}
