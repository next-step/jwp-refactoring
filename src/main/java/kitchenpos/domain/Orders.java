package kitchenpos.domain;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Embeddable
public class Orders {
    @OneToMany(mappedBy = "orderTable", cascade = {CascadeType.PERSIST, CascadeType.REFRESH})
    private List<Order> orders = new ArrayList<>();

    protected Orders() {
    }

    public Orders(List<Order> orders) {
        this.orders = orders;
    }

    public boolean isOrderStarted() {
        for (Order order : orders) {
            if (order.isStarted()) {
                return true;
            }
        }

        return false;
    }

    public void add(Order order) {
        orders.add(order);
    }
}
