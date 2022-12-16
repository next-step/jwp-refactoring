package kitchenpos.dto;

import kitchenpos.domain.Menu;
import kitchenpos.domain.Name;

public class OrderLineItemResponse {
    private Name menuName;
    private Long quantity;

    public OrderLineItemResponse() {
    }

    public OrderLineItemResponse(Menu menu, Long quantity) {
        this.menuName = menu.getName();
        this.quantity = quantity;
    }

    public Name getMenuName() {
        return menuName;
    }

    public Long getQuantity() {
        return quantity;
    }
}
