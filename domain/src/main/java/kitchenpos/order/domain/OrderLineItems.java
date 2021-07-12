package kitchenpos.order.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

@Embeddable
public class OrderLineItems {

    public OrderLineItems() {}

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderLineItem> orderLineItems = new ArrayList<>();

    public void add(OrderLineItem orderLineItem) {
        orderLineItems.add(orderLineItem);
    }

    public int size() {
        return orderLineItems.size();
    }

    public List<OrderLineItem> get() {
        return orderLineItems;
    }
}
