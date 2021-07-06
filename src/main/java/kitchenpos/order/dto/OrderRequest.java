package kitchenpos.order.dto;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.util.CollectionUtils;

import kitchenpos.order.exception.NoSuchOrderLinesException;
import kitchenpos.product.constant.OrderStatus;

public class OrderRequest {

    private Long orderTableId;
    private OrderStatus orderStatus;
    private List<OrderLineItemRequest> orderLineItems;

    protected OrderRequest() {}

    public OrderRequest(Long orderTableId, OrderStatus orderStatus, List<OrderLineItemRequest> orderLineItems) {
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderLineItems = orderLineItems;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public List<OrderLineItemRequest> getOrderLineItems() {
        return orderLineItems;
    }

    public List<Long> getOrderLineItemsMenuIds() {
        if (CollectionUtils.isEmpty(orderLineItems)) {
            throw new NoSuchOrderLinesException();
        }

        return orderLineItems.stream()
            .map(orderItem -> orderItem.getMenuId())
            .collect(Collectors.toList());
    }

}
