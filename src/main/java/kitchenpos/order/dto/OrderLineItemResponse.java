package kitchenpos.order.dto;

import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderLineItemEntity;

import java.util.List;
import java.util.stream.Collectors;

public class OrderLineItemResponse {
    private Long seq;
    private Long orderId;
    private Long menuId;
    private long quantity;

    public OrderLineItemResponse() {
    }

    public OrderLineItemResponse(Long seq, Long orderId, Long menuId, long quantity) {
        this.seq = seq;
        this.orderId = orderId;
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public static List<OrderLineItemResponse> ofList(List<OrderLineItemEntity> orderLineItems) {
        return orderLineItems.stream()
                .map(OrderLineItemResponse::of)
                .collect(Collectors.toList());
    }

    public static OrderLineItemResponse of(OrderLineItemEntity orderLineItemEntity) {
        return new OrderLineItemResponse(orderLineItemEntity.getSeq()
                , orderLineItemEntity.getOrderId()
                , orderLineItemEntity.getMenuId()
                , orderLineItemEntity.getQuantity());
    }

    public Long getSeq() {
        return seq;
    }

    public Long getOrderId() {
        return orderId;
    }

    public Long getMenuId() {
        return menuId;
    }

    public long getQuantity() {
        return quantity;
    }
}
