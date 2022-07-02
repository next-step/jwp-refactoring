package kitchenpos.helper;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.order.consts.OrderStatus;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderLineItems;

public class OrderBuilder {

    private Long id;
    private OrderStatus orderStatus;
    private LocalDateTime orderedTime;
    private List<OrderLineItem> orderLineItems;
    private Long orderTableId;


    private OrderBuilder() {
    }

    public static OrderBuilder builder() {
        return new OrderBuilder();
    }

    public OrderBuilder id(Long id) {
        this.id = id;
        return this;
    }

    public OrderBuilder orderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
        return this;
    }

    public OrderBuilder orderedTime(LocalDateTime orderedTime) {
        this.orderedTime = orderedTime;
        return this;
    }

    public OrderBuilder orderLineItems(List<OrderLineItem> orderLineItems) {
        this.orderLineItems = orderLineItems;
        return this;
    }

    public OrderBuilder orderTableId(Long orderTableId) {
        this.orderTableId = orderTableId;
        return this;
    }

    public Order build() {
        return new Order(id, orderStatus, orderedTime, orderTableId, new OrderLineItems(orderLineItems));
    }

}
