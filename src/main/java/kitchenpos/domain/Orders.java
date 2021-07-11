package kitchenpos.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

@Embeddable
public class Orders {
    @OneToMany(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "order_table_id", nullable = false, foreignKey = @ForeignKey(name = "fk_orders_order_table"))
    private List<Order> orders = new ArrayList<>();

    protected Orders() {
    }

    Orders(List<Order> orders) {
        this.orders = orders;
    }

    public void add(Order order) {
        orders.add(order);
    }

    public boolean hasOrderInProgress() {
        return orders.stream().anyMatch(Order::inProgress);
    }

    public boolean contains(Order order) {
        return orders.contains(order);
    }

    public <T> List<T> mapList(Function<Order, T> function) {
        return orders.stream()
            .map(function)
            .collect(Collectors.toList());
    }
}
