package kitchenpos.order.domain;

import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Embeddable
public class OrderLineItems {

    @OneToMany(fetch = FetchType.LAZY)
    private List<OrderLineItem> orderLineItems = new ArrayList<>();

    public List<OrderLineItem> value() {
        return orderLineItems;
    }

    public void add(OrderLineItem orderLineItem) {
        orderLineItems.add(orderLineItem);
    }
}
