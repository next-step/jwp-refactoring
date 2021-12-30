package kitchenpos.order.dto;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

import static kitchenpos.order.domain.OrderStatus.COOKING;

public class OrderRequest {
    @NotNull
    private Long tableId;
    @NotEmpty
    private List<OrderLineItemRequest> items;

    public OrderRequest(Long tableId, List<OrderLineItemRequest> items) {
        this.tableId = tableId;
        this.items = items;
    }

    public static OrderRequest of(Long orderTableId, List<OrderLineItemRequest> items) {
        return new OrderRequest(orderTableId, items);
    }

    public Order toEntity() {
        return Order.of(tableId, COOKING, getOrderLineItems(items));
    }

    private List<OrderLineItem> getOrderLineItems(List<OrderLineItemRequest> requests) {
        return requests.stream()
                .map(OrderLineItemRequest::toEntity)
                .collect(Collectors.toList());
    }

    public Long getTableId() {
        return tableId;
    }

    public List<OrderLineItemRequest> getItems() {
        return items;
    }
}
