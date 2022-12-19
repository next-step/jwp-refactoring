package kitchenpos.order.dto;

import com.fasterxml.jackson.annotation.JsonGetter;

import kitchenpos.order.domain.ItemQuantity;
import kitchenpos.product.domain.Name;

public class OrderLineItemResponse {
    private Name name;
    private ItemQuantity quantity;

    public OrderLineItemResponse() {
    }

    public OrderLineItemResponse(Name name, ItemQuantity quantity) {
        this.name = name;
        this.quantity = quantity;
    }

    public Name getName() {
        return name;
    }

    public ItemQuantity getQuantity() {
        return quantity;
    }

    @JsonGetter("name")
    public String name() {
        return name.value();
    }

    @JsonGetter("quantity")
    public Long quantity() {
        return quantity.value();
    }
}
