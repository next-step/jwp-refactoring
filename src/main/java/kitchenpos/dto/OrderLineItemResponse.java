package kitchenpos.dto;

import kitchenpos.domain.Menu;

public class OrderLineItemResponse {
    private String menuName;
    private Long quantity;

    public OrderLineItemResponse() {
    }

    public OrderLineItemResponse(Menu menu, Long quantity) {
        this.menuName = menu.getName();
        this.quantity = quantity;
    }

    public String getMenuName() {
        return menuName;
    }

    public Long getQuantity() {
        return quantity;
    }
}
