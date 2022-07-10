package kitchenpos.order.domain;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Embeddable
public class OrderLineItems {

    @OneToMany(mappedBy = "order", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<OrderLineItem> value = new ArrayList<>();

    protected OrderLineItems() {}

    public OrderLineItems(List<OrderLineItem> value) {
        this.value.addAll(value);
    }

    public void addOrderLineItems(OrderLineItems orderLineItems) {
        this.value.addAll(orderLineItems.value);
    }

    public List<OrderLineItem> getValue() {
        return value;
    }

    public void associateOrder(Order order) {
        this.value.forEach(orderLineItem -> orderLineItem.associateOrder(order));
    }
}
