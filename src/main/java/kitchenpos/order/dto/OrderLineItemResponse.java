package kitchenpos.order.dto;

import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.menu.dto.MenuResponse;

public class OrderLineItemResponse {
    private Long seq;
    private MenuResponse menuResponse;
    private OrderResponse orderResponse;
    private Long quantity;

    public OrderLineItemResponse() {
    }

    public OrderLineItemResponse(Long seq, MenuResponse menuResponse, OrderResponse orderResponse, Long quantity) {
        this.seq = seq;
        this.menuResponse = menuResponse;
        this.orderResponse = orderResponse;
        this.quantity = quantity;
    }

    public static OrderLineItemResponse of(OrderLineItem orderLineItem) {
        return new OrderLineItemResponse(orderLineItem.getSeq(), MenuResponse.of(orderLineItem.getMenu()),
                OrderResponse.of(orderLineItem.getOrder()), orderLineItem.getQuantity());
    }

    public Long getSeq() {
        return seq;
    }

    public MenuResponse getMenuResponse() {
        return menuResponse;
    }

    public OrderResponse getOrderResponse() {
        return orderResponse;
    }

    public Long getQuantity() {
        return quantity;
    }
}
