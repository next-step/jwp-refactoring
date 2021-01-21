package kitchenpos.order.dto;

import kitchenpos.order.OrderLineItem;

public class OrderLineItemResponse {
    private static final String ERR_TEXT_INVALID_ORDER_LINE_ITEM = "유효하지 않은 데이터입니다.";

    private Long id;
    private Long orderId;
    private Long menuId;
    private Long quantity;

    protected OrderLineItemResponse() {
    }

    protected OrderLineItemResponse(final OrderLineItem orderLineItem) {
        this.id = orderLineItem.getId();
        this.orderId = orderLineItem.getOrderId();
        this.menuId = orderLineItem.getMenuId();
        this.quantity = orderLineItem.getQuantity();
    }

    public static OrderLineItemResponse of(final OrderLineItem orderLineItem) {
        if (orderLineItem == null) {
            throw new IllegalArgumentException(ERR_TEXT_INVALID_ORDER_LINE_ITEM);
        }

        return new OrderLineItemResponse(orderLineItem);
    }

    public Long getId() {
        return id;
    }

    public Long getOrderId() {
        return orderId;
    }

    public Long getMenuId() {
        return menuId;
    }

    public Long getQuantity() {
        return quantity;
    }
}
