package kitchenpos.dto;

import kitchenpos.domain.OrderLineItemEntity;

import java.util.List;
import java.util.stream.Collectors;

public class OrderLineItemResponse {
    private Long seq;
    private Long orderId;
    private Long menuId;
    private long quantity;

    protected OrderLineItemResponse(Long seq, Long orderId, Long menuId, long quantity) {
        this.seq = seq;
        this.orderId = orderId;
        this.menuId = menuId;
        this.quantity = quantity;
    }

    protected OrderLineItemResponse() {
    }

    public static OrderLineItemResponse of(OrderLineItemEntity orderLineItem) {
        return new OrderLineItemResponse(orderLineItem.getSeq(), orderLineItem.getOrder().getId(),
                                         orderLineItem.getMenuId(), orderLineItem.getQuantity().getValue());
    }

    public static List<OrderLineItemResponse> of(List<OrderLineItemEntity> orderLineItems) {
        return orderLineItems.stream()
                             .map(OrderLineItemResponse::of)
                             .collect(Collectors.toList());
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
