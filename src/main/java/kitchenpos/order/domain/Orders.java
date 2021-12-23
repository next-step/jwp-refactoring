package kitchenpos.order.domain;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Embeddable
public class Orders {
    @OneToMany(mappedBy = "orderTable", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private List<Order> orders = new ArrayList<>();

    protected Orders() {
    }

    public void add(Order addOrder) {
        orders.add(addOrder);
    }

    public List<Order> getOrders() {
        return Collections.unmodifiableList(orders);
    }

    public void validateCompletion() {
        orders.stream()
                .forEach(order -> order.validateCompletion());
    }
}
