package kitchenpos.dto;

import kitchenpos.domain.Menu;
import kitchenpos.domain.OrderLineItem;

import java.util.List;

import static kitchenpos.constants.ErrorCodeType.MATCH_NOT_MENU;

public class OrderLineItemRequest {
    private Long menuId;
    private Long quantity;

    protected OrderLineItemRequest() {}

    public OrderLineItemRequest(Long menuId, Long quantity) {
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public OrderLineItem createOrderLineItemRequest(List<Menu> menus) {
        Menu menu = menus.stream()
                .filter(target -> target.getId().equals(menuId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(MATCH_NOT_MENU.getMessage()));

        return new OrderLineItem(menu, quantity);
    }
    public Long getMenuId() {
        return menuId;
    }

    public Long getQuantity() {
        return quantity;
    }
}
