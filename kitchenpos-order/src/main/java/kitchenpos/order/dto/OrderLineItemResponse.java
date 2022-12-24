package kitchenpos.order.dto;

import com.fasterxml.jackson.annotation.JsonGetter;

import kitchenpos.common.domain.Name;
import kitchenpos.common.domain.Quantity;

public class OrderLineItemResponse {
    private Name name;
    private Quantity quantity;

    public OrderLineItemResponse() {
    }

    public OrderLineItemResponse(Name name, Quantity quantity) {
        this.name = name;
        this.quantity = quantity;
    }

    public Name getName() {
        return name;
    }

    public Quantity getQuantity() {
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
