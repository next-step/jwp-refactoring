package kitchenpos.order.domain;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

@Embeddable
public class OrderLineItems {

    @OneToMany(mappedBy = "order",cascade = CascadeType.ALL)
    private final List<OrderLineItem> orderLineItems = new ArrayList<>();

    public void addOrderLineItems(OrderLineItem orderLineItem) {
        orderLineItems.add(orderLineItem);
    }
}
