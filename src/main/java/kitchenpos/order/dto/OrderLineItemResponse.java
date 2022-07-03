package kitchenpos.order.dto;

import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.order.domain.OrderLineItem;

public class OrderLineItemResponse {
    private Long id;
    private MenuResponse menu;
    private Long quantity;

    protected OrderLineItemResponse() {}

    public OrderLineItemResponse(Long id, MenuResponse menu, Long quantity) {
        this.id = id;
        this.menu = menu;
        this.quantity = quantity;
    }

    public static OrderLineItemResponse from(OrderLineItem orderLineItem) {
        return new OrderLineItemResponse(
                orderLineItem.getSeq(),
                MenuResponse.from(orderLineItem.getMenu()),
                orderLineItem.getQuantity().getValue()
        );
    }

    public Long getId() {
        return id;
    }

    public MenuResponse getMenu() {
        return menu;
    }

    public Long getQuantity() {
        return quantity;
    }
}
