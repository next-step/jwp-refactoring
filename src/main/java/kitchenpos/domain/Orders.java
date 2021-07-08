package kitchenpos.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

@Embeddable
public class Orders {
    @OneToMany(mappedBy = "orderTable", cascade = CascadeType.ALL, orphanRemoval = true)
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

    public <T> List<T> mapList(Function<Order, T> function) {
        return orders.stream()
            .map(function)
            .collect(Collectors.toList());
    }
}
