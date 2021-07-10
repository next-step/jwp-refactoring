package kitchenpos.order.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

@Embeddable
public class Orders {

    @OneToMany(mappedBy = "orderTableId", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<Order> data = new ArrayList<>();

    public Orders() { }

    public Orders(Collection<Order> orders) {
        orders.forEach(this::add);
    }

    public void add(Order order) {
        data.add(order);
    }

    public boolean hasCookingOrMealOrder() {
        return data.stream().anyMatch(Order::hasNotCompletedOrder);
    }

    public List<Order> getData() {
        return Collections.unmodifiableList(data);
    }
}
