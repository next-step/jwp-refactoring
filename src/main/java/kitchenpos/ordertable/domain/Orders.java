package kitchenpos.ordertable.domain;

import kitchenpos.order.domain.Order;

import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Embeddable
public class Orders {

    public static final String MESSAGE_VALIDATE_ORDER = "주문이 변경 가능한 상태여야 합니다.";

    @OneToMany(mappedBy = "orderTable")
    private List<Order> orders = new ArrayList<>();

    protected Orders() {
    }

    public Orders(List<Order> orders) {
        this.orders = orders;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void validateEmptyChangable() {
        orders.forEach(this::validateOrder);
    }

    boolean isChangable() {
        return orders.stream()
                .allMatch(Order::isChangable);
    }

    void add(Order order) {
        orders.add(order);
    }

    private void validateOrder(Order order) {
        if (!order.isChangable()) {
            throw new IllegalArgumentException(MESSAGE_VALIDATE_ORDER);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Orders orders1 = (Orders) o;
        return Objects.equals(orders, orders1.orders);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orders);
    }
}
