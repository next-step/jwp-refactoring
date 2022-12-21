package kitchenpos.order.dto;

import java.util.List;
import kitchenpos.common.domain.Quantity;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderMenu;

public class OrderLineItemRequest {
    private Long menuId;
    private Long quantity;

    protected OrderLineItemRequest() {}

    public OrderLineItemRequest(Long menuId, Long quantity) {
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public static OrderLineItemRequest of(Long menuId, Long quantity) {
        return new OrderLineItemRequest(menuId, quantity);
    }

    public OrderLineItem createOrderLineItem(List<OrderMenu> menus) {
        OrderMenu menu = menus.stream()
                .filter(item -> item.getId().equals(menuId))
                .findFirst()
                .get();

        return new OrderLineItem(new Quantity(quantity), menu);
    }

    public Long getMenuId() {
        return menuId;
    }

    public Long getQuantity() {
        return quantity;
    }
}
