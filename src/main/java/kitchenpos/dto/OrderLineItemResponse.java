package kitchenpos.dto;

import kitchenpos.domain.OrderLineItem;

public class OrderLineItemResponse {
    private MenuResponse menu;
    private long quantity;

    public static OrderLineItemResponse of(OrderLineItem orderLineItem) {
        return new OrderLineItemResponse(MenuResponse.of(orderLineItem.getMenu()), orderLineItem.getQuantity().value());
    }

    public OrderLineItemResponse() {
    }

    public OrderLineItemResponse(MenuResponse menu, long quantity) {
        this.menu = menu;
        this.quantity = quantity;
    }

    public MenuResponse getMenu() {
        return menu;
    }

    public long getQuantity() {
        return quantity;
    }
}
