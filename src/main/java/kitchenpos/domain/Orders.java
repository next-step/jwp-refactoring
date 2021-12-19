package kitchenpos.domain;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Embeddable
public class Orders {
    @OneToMany(mappedBy = "orderTable", cascade = {CascadeType.PERSIST, CascadeType.REFRESH})
    private List<Order> orders = new ArrayList<>();

    protected Orders() {
    }

    public Orders(List<Order> orders) {
        this.orders = orders;
    }

    public boolean containsStartedOrder() {
        return orders.stream()
                .anyMatch(Order::isStarted);
    }

    public void add(Order order) {
        orders.add(order);
    }
}
