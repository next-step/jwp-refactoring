package kitchenpos.order.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class OrderCreateRequest {
    private Long orderTable;
    private List<OrderLineItemRequest> orderLineItems = new ArrayList<>();

    protected OrderCreateRequest() {}

    public OrderCreateRequest(Long orderTable, List<OrderLineItemRequest> orderLineItems) {
        this.orderTable = orderTable;
        this.orderLineItems.addAll(orderLineItems);
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
