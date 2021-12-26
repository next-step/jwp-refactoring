package kitchenpos.order.dto;

import kitchenpos.menu.domain.Menu;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

public class OrderLineItemRequest {

    @Positive
    @NotNull
    private Long menuId;

    @Positive
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
