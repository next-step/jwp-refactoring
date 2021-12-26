package kitchenpos.order.domain;

import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

/**
 * packageName : kitchenpos.order.domain
 * fileName : Orders
 * author : haedoang
 * date : 2021-12-22
 * description : 주문 일급컬렉션
 */
@Embeddable
public class Orders {
    @OneToMany(mappedBy = "orderTableId")
    private final List<Order> orders = new ArrayList<>();

    public void add(Order order) {
        orders.add(order);
    }

    public boolean checkOccupied() {
        return !orders.isEmpty() && !orderCompleted();
    }

    public boolean orderCompleted() {
        return orders.stream()
                .allMatch(Order::isCompleted);
    }

    public List<Order> value() {
        return orders;
    }
}
