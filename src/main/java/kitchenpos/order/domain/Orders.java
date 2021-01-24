package kitchenpos.order.domain;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Embeddable
public class Orders {

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "order_table_id")
    private List<Order> orders = new ArrayList<>();

    protected Orders() {
    }

    public boolean hasNotComplete() {
        return orders.stream()
                .anyMatch(Order::isNotComplete);
    }
}
