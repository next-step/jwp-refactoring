package domain.order;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Embeddable
public class Orders {

    @Fetch(FetchMode.SELECT)
    @BatchSize(size = 100)
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "order_table_id")
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

    public boolean hasCookingOrMeal() {
        return orders.stream().anyMatch(Order::isCookingOrMeal);
    }

    public Order newOrder(Long orderTableId, OrderLineItems orderLineItems) {
        Order cookingOrder = Order.of(orderTableId, OrderStatus.COOKING, orderLineItems);
        orders.add(cookingOrder);
        return cookingOrder;
    }

    public List<Order> getUnmodifiableList() {
        return Collections.unmodifiableList(orders);
    }
}
