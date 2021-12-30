package kitchenpos.moduledomain.order;

import static javax.persistence.FetchType.LAZY;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.Embeddable;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

@Embeddable
public class Orders {

    @OneToMany(fetch = LAZY)
    @JoinColumn(name = "order_table_id", foreignKey = @ForeignKey(name = "fk_order_order_table_id"))
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
