package kitchenpos.domain.order.domain;

import kitchenpos.domain.order.exception.IllegalOrderLineItemException;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Embeddable
public class OrderLineItemGroup {
    private static final String EMPTY_ERROR_MESSAGE = "주문 항목은 비어있을 수 없습니다.";
    @OneToMany(fetch = FetchType.LAZY, orphanRemoval = true, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "order_id", nullable = false, updatable = false)
    private final List<OrderLineItem> orderLineItems = new ArrayList<>();

    protected OrderLineItemGroup() {
    }

    private OrderLineItemGroup(List<OrderLineItem> orderLineItems) {
        validate(orderLineItems);
        this.orderLineItems.addAll(orderLineItems);
    }

    public static OrderLineItemGroup of(List<OrderLineItem> orderLineItems) {
        return new OrderLineItemGroup(orderLineItems);
    }

    private void validate(List<OrderLineItem> orderLineItems) {
        if (Objects.isNull(orderLineItems) || orderLineItems.isEmpty()) {
            throw new IllegalOrderLineItemException(EMPTY_ERROR_MESSAGE);
        }

    }

    public List<OrderLineItem> getOrderLineItems() {
        return Collections.unmodifiableList(orderLineItems);
    }

    public List<Long> getMenuIds() {
        return this.orderLineItems.stream()
                .map(OrderLineItem::getMenuId)
                .collect(Collectors.toList());
    }
}
