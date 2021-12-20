package kitchenpos.order.domain;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import javax.persistence.OneToMany;

@Embeddable
public final class Orders {

    @OneToMany(mappedBy = "orderTable", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<Order> orders = new ArrayList<>();

    protected Orders() {
    }

    public static Orders create() {
        return new Orders();
    }

    public void add(Order order) {
        if (!orders.contains(order)) {
            orders.add(order);
        }
    }

    public void remove(Order order) {
        orders.remove(order);
    }

    //주문이 진행중인(조리,식사) 테이블은 상태를 변경할 수 없다
    public boolean isOnGoing() {
        return orders.stream()
            .anyMatch(Order::isOnGoing);

    }
}
