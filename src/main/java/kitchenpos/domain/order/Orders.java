package kitchenpos.domain.order;

import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Embeddable
public class Orders {
    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_table_id")
    private List<Order> orders = new ArrayList<>();

    public Orders() {
    }

    public Orders(List<Order> orders) {
        orders.stream()
                .forEach(item -> this.orders.add(item));
    }

    public boolean isAllFinished() {
        boolean isAllFinished = orders.stream()
                .allMatch(Order::isFinished);

        return isAllFinished;
    }

    public List<Order> toCollection() {
        return Collections.unmodifiableList(orders);
    }
}
