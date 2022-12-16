package kitchenpos.dto;

import com.fasterxml.jackson.annotation.JsonGetter;

import kitchenpos.domain.Menu;
import kitchenpos.domain.Name;
import kitchenpos.domain.Quantity;

public class OrderLineItemResponse {
    private Name menuName;
    private Quantity quantity;

    public OrderLineItemResponse() {
    }

    public OrderLineItemResponse(Menu menu, Quantity quantity) {
        this.menuName = menu.getName();
        this.quantity = quantity;
    }

    public Name getMenuName() {
        return menuName;
    }

    public Quantity getQuantity() {
        return quantity;
    }

    @JsonGetter("quantity")
    public Long quantity() {
        return quantity.value();
    }
}
