package kitchenpos.order.dto;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.table.domain.OrderTable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class OrderCreateRequest {
    private Long orderTable;
    private List<OrderLineItemRequest> orderLineItems = new ArrayList<>();

    protected OrderCreateRequest() {}

    public OrderCreateRequest(Long orderTable, List<OrderLineItemRequest> orderLineItems) {
        this.orderTable = orderTable;
        this.orderLineItems.addAll(orderLineItems);
    }

    public Order of(OrderTable orderTable, Function<OrderLineItemRequest, OrderLineItem> converter) {
        List<OrderLineItem> orderLineItems = this.orderLineItems.stream()
                .map(converter)
                .collect(Collectors.toList());

        return new Order(orderTable, orderLineItems);
    }

    public Long getOrderTable() {
        return orderTable;
    }

    public List<OrderLineItemRequest> getOrderLineItems() {
        return orderLineItems;
    }

    public List<Long> getMenus() {
        return orderLineItems.stream()
                .map(OrderLineItemRequest::getMenu)
                .collect(Collectors.toList());
    }
}
