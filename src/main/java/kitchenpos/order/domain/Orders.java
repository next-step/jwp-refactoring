package kitchenpos.order.domain;

import static javax.persistence.FetchType.LAZY;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

@Embeddable
public class Orders {

    @OneToMany(mappedBy = "orderTable", fetch = LAZY)
    private List<Order> orders = new ArrayList<>();

    public Orders(List<Order> orders) {
        this.orders.addAll(orders);
    }

    public Orders() {
    }

    public void validCookieOrMeal() {
        orders.stream()
            .forEach(s -> OrderStatus.validStatusIsCookingOrMealThrow(s.getOrderStatus()));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Orders orders1 = (Orders) o;
        return Objects.equals(orders, orders1.orders);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orders);
    }
}
