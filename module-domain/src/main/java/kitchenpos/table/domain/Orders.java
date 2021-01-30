package kitchenpos.table.domain;

import kitchenpos.order.domain.Order;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Embeddable
public class Orders {
    @OneToMany(mappedBy = "orderTable", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<Order> orders = new ArrayList<>();

    protected Orders() {
    }

    public Orders(List<Order> orders) {
        this.orders = orders;
    }

    public boolean checkChangeable() {
        return orders.stream()
                .anyMatch(Order::changeable);
    }

    public void add(Order order) {
        orders.add(order);
    }

    public List<Order> getOrders() {
        return orders;
    }

}
