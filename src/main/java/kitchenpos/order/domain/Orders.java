package kitchenpos.order.domain;

import org.springframework.data.annotation.ReadOnlyProperty;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Embeddable
public class Orders {

    @OneToMany(mappedBy = "orderTable", cascade = CascadeType.ALL)
    @ReadOnlyProperty
    private final List<Order> orders;

    protected Orders() {
        this.orders = new ArrayList<>();
    }

    private Orders(List<Order> orders) {
        this.orders = orders;
    }

    public static Orders of(List<Order> orders) {
        return new Orders(orders);
    }

    public void validateAllNotCompletionStatus() {
        orders.forEach(Order::validateNotCompletionStatus);
    }

    public void add(Order order) {
        orders.add(order);
    }

    public List<Order> getUnmodifiableList(){
        return Collections.unmodifiableList(orders);
    }
}
