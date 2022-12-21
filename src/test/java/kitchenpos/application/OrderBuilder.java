package kitchenpos.application;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;

class OrderBuilder {
    private Long orderTableId;
    private String orderStatus;
    private List<OrderLineItem> orderLineItems = new ArrayList<>();

    OrderBuilder withOrderTable(OrderTable orderTable) {
        this.orderTableId = orderTable.getId();
        return this;
    }

    OrderBuilder withStatus(OrderStatus status) {
        this.orderStatus = status.name();
        return this;
    }

    OrderBuilder withOrderLineItems(List<OrderLineItem> orderLineItems) {
        this.orderLineItems = orderLineItems;
        return this;
    }

    Order build() {
        return new Order(orderTableId, orderStatus, LocalDateTime.now(), orderLineItems);
    }

    static OrderBuilder anOrder() {
        return new OrderBuilder();
    }

    static Order anOrderWithStatus(Long orderTableId, OrderStatus orderStatus) {
        return new Order(orderTableId, orderStatus.name(), LocalDateTime.now(), Collections.emptyList());
    }

    static Order cookingStatusOrder(Long orderTableId) {
        return anOrderWithStatus(orderTableId, OrderStatus.COOKING);
    }

    static Order mealStatusOrder(Long orderTableId) {
        return anOrderWithStatus(orderTableId, OrderStatus.MEAL);
    }

    static Order completionStatusOrder(Long orderTableId) {
        return anOrderWithStatus(orderTableId, OrderStatus.COMPLETION);
    }

}
