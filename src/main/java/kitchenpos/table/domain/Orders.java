package kitchenpos.table.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import kitchenpos.order.domain.Order;

@Embeddable
public class Orders {

    @OneToMany(mappedBy = "id", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<Order> data = new ArrayList<>();

    public Orders() { }

    public Orders(Collection<Order> orders) {
        orders.forEach(this::add);
    }

    public void add(Order order) {
        data.add(order);
    }

    public boolean hasNotCompletedOrder() {
        return data.stream().allMatch(Order::isCompletedOrder);
    }

    public List<Order> getData() {
        return data;
    }
}
