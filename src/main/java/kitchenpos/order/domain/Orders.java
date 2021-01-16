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

    public boolean hasUnchangeableStatusOrder() {
        return orders.stream().anyMatch(order -> OrderStatus.isUnchangeableStatus(order.getOrderStatus()));
    }
}
