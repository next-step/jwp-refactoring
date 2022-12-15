package kitchenpos.domain.order;

import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Embeddable
public class Orders {

    @OneToMany(mappedBy = "orderTable")
    private List<Order> orders = new ArrayList<>();

    protected Orders() {
    }

    private Orders(List<Order> orders) {
        this.orders = orders;
    }

    public static Orders from(List<Order> orders) {
        return new Orders(orders);
    }

    public static Orders createEmpty() {
        return new Orders(new ArrayList<>());
    }

    public List<Order> getOrders() {
        return Collections.unmodifiableList(orders);
    }


    public void add(Order order) {
        if (this.orders.contains(order)) {
            return;
        }

        this.orders.add(order);
    }
}
