package kitchenpos.order.dto;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderLineItems;

public class OrderLineItemResponse {


    private Long seq;

    private Long orderId;

    private Long menuId;

    private int quantity;

    public OrderLineItemResponse() {
    }

    private OrderLineItemResponse(Long seq, Long orderId, Long menuId, int quantity) {
        this.seq = seq;
        this.orderId = orderId;
        this.menuId = menuId;
        this.quantity = quantity;
    }


    public static OrderLineItemResponse of(OrderLineItem orderLineItem){
        return new OrderLineItemResponse(orderLineItem.getSeq(),
                orderLineItem.getOrder().getId(),
                orderLineItem.getMenu().getId(),
                orderLineItem.getQuantity().value());

    }

    public static List<OrderLineItemResponse> of(OrderLineItems orderLineItems) {
        return orderLineItems.getOrderLineItems().stream()
                .map(OrderLineItemResponse::of).collect(Collectors.toList());
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

    public int getQuantity() {
        return quantity;
    }
}
