package kitchenpos.order.domain;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

@Embeddable
public class OrderLineItems {
    @OneToMany(fetch = FetchType.LAZY)
    private List<OrderLineItem> elements = new ArrayList<>();

    protected OrderLineItems() {
    }

    public void add(OrderLineItem orderLineItem) {
        elements.add(orderLineItem);
    }

    public List<OrderLineItem> get() {
        return elements;
    }
}
