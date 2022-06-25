package kitchenpos.order.domain;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

@Embeddable
public class OrderLineItems {
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderLineItem> orderLineItems;

    protected OrderLineItems() {
    }

    private OrderLineItems(List<OrderLineItem> orderLineItems) {
        validateOrderLineItems(orderLineItems);
        this.orderLineItems = orderLineItems;
    }

    public static OrderLineItems from(List<OrderLineItem> orderLineItems) {
        return new OrderLineItems(orderLineItems);
    }

    private static void validateOrderLineItems(List<OrderLineItem> orderLineItems) {
        if (Objects.isNull(orderLineItems)) {
            throw new IllegalArgumentException("주문 항목이 필요합니다.");
        }
    }

    public List<OrderLineItem> readOnlyOrderLineItems() {
        return Collections.unmodifiableList(this.orderLineItems);
    }
}

