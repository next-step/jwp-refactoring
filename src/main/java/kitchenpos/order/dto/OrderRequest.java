package kitchenpos.order.dto;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;

public class OrderRequest {

    private Long orderTableId;
    private List<OrderLineRequest> orderLineItems;

    public OrderRequest(Long orderTableId, List<OrderLineRequest> orderLineItems) {
        this.orderTableId = orderTableId;
        this.orderLineItems = orderLineItems;
    }

    public OrderRequest() {
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public List<OrderLineRequest> getOrderLineItems() {
        return orderLineItems;
    }

    public Order toOrder() {
        return new Order(orderTableId,
            orderLineItems.stream().map(s -> new OrderLineItem(s.getMenuId(), s.getQuantity())).collect(
                Collectors.toList()));
    }
}
