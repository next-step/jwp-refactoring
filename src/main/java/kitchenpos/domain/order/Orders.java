package kitchenpos.domain.order;

import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Embeddable
public class Orders {
    @OneToMany(fetch = FetchType.LAZY)
    private List<Order> orders = new ArrayList<>();

    public Orders() {
    }

    public Orders(List<Order> orders) {
        this.orders = orders;
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
