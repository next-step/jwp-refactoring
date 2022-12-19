package kitchenpos.order.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

@Embeddable
public class OrderLineItems {

    @OneToMany(mappedBy = "order", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<OrderLineItem> orderLineItems = new ArrayList<>();

    protected OrderLineItems() {
    }

    public void add(OrderLineItem orderLineItem) {
        orderLineItems.add(orderLineItem);
    }

    public List<OrderLineItem> getUnmodifiableOrderLineItems() {
        return Collections.unmodifiableList(orderLineItems);
    }
}
