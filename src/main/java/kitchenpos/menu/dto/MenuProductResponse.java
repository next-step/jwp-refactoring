package kitchenpos.menu.dto;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;

import kitchenpos.menu.domain.Quantity;
import kitchenpos.product.domain.Name;
import kitchenpos.product.domain.Price;

public class MenuProductResponse {
    private Name name;
    private Price price;
    private Quantity quantity;

    public MenuProductResponse() {
    }

    public MenuProductResponse(Name name, Price price, Quantity quantity) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    public Name getName() {
        return name;
    }

    public Price getPrice() {
        return price;
    }

    public Quantity getQuantity() {
        return quantity;
    }

    @JsonGetter("name")
    public String name() {
        return name.value();
    }

    @JsonGetter("price")
    public BigDecimal price() {
        return price.value();
    }

    @JsonGetter("quantity")
    public Long quantity() {
        return quantity.value();
    }

    @JsonSetter("price")
    public void setPrice(BigDecimal price) {
        this.price = new Price(price);
    }
}
