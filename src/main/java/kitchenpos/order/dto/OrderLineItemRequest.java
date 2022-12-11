package kitchenpos.order.dto;

import kitchenpos.common.domain.Quantity;
import kitchenpos.menu.domain.Menu;
import kitchenpos.order.domain.OrderLineItem;

import java.util.List;

public class OrderLineItemRequest {
    private Long menuId;
    private Long quantity;

    protected OrderLineItemRequest() {}

    private OrderLineItemRequest(Long menuId, Long quantity) {
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public static OrderLineItemRequest of(Long menuId, Long quantity) {
        return new OrderLineItemRequest(menuId, quantity);
    }

    public Long getMenuId() {
        return menuId;
    }

    public Long getQuantity() {
        return quantity;
    }

    public OrderLineItem createOrderLineItem(List<Menu> menus) {
        Menu menu = menus.stream()
                .filter(item -> item.getId().equals(menuId))
                .findFirst()
                .get();

        return new OrderLineItem(new Quantity(quantity), menu);
    }
}
