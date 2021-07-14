package kitchenpos.application.ordertable;

import kitchenpos.application.order.Order;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Embeddable
public class Orders {
    @OneToMany(mappedBy = "orderTable", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Order> orders = new ArrayList<>();

    protected Orders() {
    }

    public Orders(List<Order> orders) {
        this.orders = orders;
    }

    public void add(Order order) {
        orders.add(order);
    }

    public boolean isNotCompletion() {
        return orders.stream()
                .anyMatch(Order::isNotCompletion);
    }
}