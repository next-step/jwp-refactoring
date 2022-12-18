package kitchenpos.order.domain;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Embeddable
public class OrderLineItems {
    @OneToMany(mappedBy = "orderId", cascade = CascadeType.ALL)
    private List<OrderLineItem> values;

    protected OrderLineItems() {
        values = new ArrayList<>();
    }

    public List<OrderLineItem> values() {
        return Collections.unmodifiableList(values);
    }

    public void add(OrderLineItem orderLineItem) {
        values.add(orderLineItem);
    }
}
