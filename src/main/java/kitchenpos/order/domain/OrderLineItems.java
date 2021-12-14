package kitchenpos.order.domain;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Embeddable
public class OrderLineItems {

    @OneToMany(mappedBy = "order", cascade = CascadeType.PERSIST)
    private List<OrderLineItem> orderLineItems = new ArrayList<>();

    protected OrderLineItems() {
    }

    public List<OrderLineItem> getOrderLineItems() {
        return orderLineItems;
    }

    void add(OrderLineItem orderLineItem) {
        orderLineItems.add(orderLineItem);
    }
}
