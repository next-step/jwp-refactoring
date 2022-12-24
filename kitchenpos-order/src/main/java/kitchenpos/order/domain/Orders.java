package kitchenpos.order.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

@Embeddable
public class Orders {

    @OneToMany(mappedBy = "orderTableId", cascade = CascadeType.ALL)
    private List<Order> orders = new ArrayList<>();

    public boolean hasCannotChangeOrder() {
        for (Order order : orders) {
            if (order.isProceeding()) {
                return true;
            }
        }
        return false;
    }

    public void add(Order order) {
        if (!this.orders.contains(order)) {
            this.orders.add(order);
        }
    }
}
