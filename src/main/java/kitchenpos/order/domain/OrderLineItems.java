package kitchenpos.order.domain;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Embeddable
public class OrderLineItems {

    private static final int MIN_ORDER_ITEM_COUNT = 1;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "order_id")
    private List<OrderLineItem> orderLineItems = new ArrayList<>();

    protected OrderLineItems() {
    }

    public OrderLineItems(final List<OrderLineItem> orderLineItems) {
        validate(orderLineItems);
        this.orderLineItems.addAll(Objects.requireNonNull(orderLineItems));
    }

    private void validate(final List<OrderLineItem> orderLineItems) {
        if (orderLineItems.size() < MIN_ORDER_ITEM_COUNT) {
            throw new IllegalArgumentException(String.format("%d개 이상 주문 항목이 있어야 합니다.", MIN_ORDER_ITEM_COUNT));
        }
    }

    public List<OrderLineItem> getOrderLineItems() {
        return orderLineItems;
    }
}
