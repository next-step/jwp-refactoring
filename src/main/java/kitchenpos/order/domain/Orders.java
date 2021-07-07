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

    public void addOrder(Order order) {
        orders.add(order);
    }

    public boolean notExistCompleteOrder() {
        return orders.stream()
            .anyMatch(order -> !order.getOrderStatus().equals(OrderStatus.COMPLETION));
    }
}
