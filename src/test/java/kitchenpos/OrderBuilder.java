package kitchenpos;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;

public class OrderBuilder {
    private Long id;
    private Long orderTableId;
    private String orderStatus;
    private List<OrderLineItem> orderLineItems = new ArrayList<>();

    public OrderBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public OrderBuilder withOrderTable(OrderTable orderTable) {
        this.orderTableId = orderTable.getId();
        return this;
    }

    public OrderBuilder withStatus(OrderStatus status) {
        this.orderStatus = status.name();
        return this;
    }

    public OrderBuilder withOrderLineItems(List<OrderLineItem> orderLineItems) {
        this.orderLineItems = orderLineItems;
        return this;
    }

    public Order build() {
        return new Order(id, orderTableId, orderStatus, LocalDateTime.now(), orderLineItems);
    }

    public static OrderBuilder anOrder() {
        return new OrderBuilder();
    }

    public static Order anOrderWithStatus(Long orderTableId, OrderStatus orderStatus) {
        return new Order(orderTableId, orderStatus.name(), LocalDateTime.now(), Collections.emptyList());
    }

    public static Order cookingStatusOrder(Long orderTableId) {
        return anOrderWithStatus(orderTableId, OrderStatus.COOKING);
    }

    public static Order mealStatusOrder(Long orderTableId) {
        return anOrderWithStatus(orderTableId, OrderStatus.MEAL);
    }

    public static Order completionStatusOrder(Long orderTableId) {
        return anOrderWithStatus(orderTableId, OrderStatus.COMPLETION);
    }

}
