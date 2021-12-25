package kitchenpos.order.dto;

import kitchenpos.menu.domain.Menu;

import javax.validation.constraints.NotNull;

public class OrderLineItemRequest {

    @NotNull
    private Long menuId;

    @NotNull
    private Long quantity;

    public Long getMenuId() {
        return menuId;
    }

    public Long getQuantity() {
        return quantity;
    }

    public boolean match(Menu menu) {
        return menu.matchId(menuId);
    }
}
