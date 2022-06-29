package kitchenpos.order.domain;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

@Embeddable
public class OrderLineItems {

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "order_id")
    private List<OrderLineItem> orderLineItems = new ArrayList<>();

    public OrderLineItems() {
    }

    public OrderLineItems(List<OrderLineItem> orderLineItems) {
        validate(orderLineItems);
        this.orderLineItems = orderLineItems;
    }

    public void add(OrderLineItem orderLineItem) {
        orderLineItems.add(orderLineItem);
    }

    public boolean isEmpty() {
        return orderLineItems.isEmpty();
    }

    public int size() {
        return orderLineItems.size();
    }

    public List<OrderLineItem> getOrderLineItems() {
        return orderLineItems;
    }

    private void validate(List<OrderLineItem> orderLineItems) {
        if (orderLineItems.isEmpty()) {
            throw new IllegalArgumentException("[ERROR] 주문 항목이 비어있습니다.");
        }
    }

}
