package kitchenpos.order.dto;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class OrderRequest {
    private Long id;
    private Long orderTableId;
    private List<OrderLineItemRequest> orderLineItemRequests;

    public OrderRequest() {
    }

    public OrderRequest(Long id, Long orderTableId, List<OrderLineItemRequest> orderLineItemRequests) {
        this.id = id;
        this.orderTableId = orderTableId;
        this.orderLineItemRequests = orderLineItemRequests;
    }

    public Long getId() {
        return id;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public List<OrderLineItemRequest> getOrderLineItemRequests() {
        return orderLineItemRequests;
    }

    public Order toOrder() {
        return Order.of(id, orderTableId, convertToOrderLineItems());
    }

    private List<OrderLineItem> convertToOrderLineItems() {
        if (Objects.isNull(orderLineItemRequests)) {
            return new ArrayList<>();
        }
        return orderLineItemRequests.stream()
                .map(OrderLineItemRequest::toOrderLineItem)
                .collect(Collectors.toList());
    }
}
