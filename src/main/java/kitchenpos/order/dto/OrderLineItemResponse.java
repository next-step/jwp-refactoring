package kitchenpos.order.dto;

import kitchenpos.order.domain.OrderLineItem;

public class OrderLineItemResponse {
    private Long id;
    private Long menu;
    private Long quantity;

    protected OrderLineItemResponse() {}

    public OrderLineItemResponse(Long id, Long menu, Long quantity) {
        this.id = id;
        this.menu = menu;
        this.quantity = quantity;
    }

    public static OrderLineItemResponse from(OrderLineItem orderLineItem) {
        return new OrderLineItemResponse(
                orderLineItem.getSeq(),
                orderLineItem.getMenuId(),
                orderLineItem.getQuantity().getValue()
        );
    }

    public Long getId() {
        return id;
    }

    public Long getMenu() {
        return menu;
    }

    public Long getQuantity() {
        return quantity;
    }
}
