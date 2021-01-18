package kitchenpos.dto;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.model.Order;
import kitchenpos.domain.model.OrderLineItem;
import kitchenpos.domain.model.OrderStatus;

public class OrderCreateRequest {

    private Long orderTableId;

    private List<OrderLineItemCreateRequest> orderLineItems = Collections.emptyList();

    public Long getOrderTableId() {
        return orderTableId;
    }

    public List<OrderLineItemCreateRequest> getOrderLineItems() {
        return orderLineItems;
    }

    protected OrderCreateRequest() {
    }

    public OrderCreateRequest(Long orderTableId, List<OrderLineItemCreateRequest> orderLineItems) {
        this.orderTableId = orderTableId;
        this.orderLineItems = orderLineItems;
    }

    public Order toEntity() {
        List<OrderLineItem> collect = orderLineItems.stream()
                .map(OrderLineItemCreateRequest::toEntity)
                .collect(Collectors.toList());
        return new Order(orderTableId, OrderStatus.COOKING, LocalDateTime.now(), collect);
    }
}
