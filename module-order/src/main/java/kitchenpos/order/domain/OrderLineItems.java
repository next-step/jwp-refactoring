package kitchenpos.order.domain;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Embeddable
public class OrderLineItems {
    private static final int MINIMUM_ITEM_SIZE = 1;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderLineItem> elements = new ArrayList<>();

    public void add(final OrderLineItem orderLineItem) {
        elements.add(orderLineItem);
    }

    public List<OrderLineItem> getElements() {
        return elements;
    }

    public void addAll(final Order order, final List<OrderLineItem> orderLineItems) {
        if (orderLineItems.size() < MINIMUM_ITEM_SIZE) {
            throw new IllegalArgumentException("주문 항목은 하나 이상이어야 합니다.");
        }

        for (OrderLineItem orderLineItem : orderLineItems) {
            elements.add(orderLineItem);
            orderLineItem.setOrder(order);
        }
    }
}
