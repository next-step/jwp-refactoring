package kitchenpos.product.dto;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;

import kitchenpos.product.domain.Name;
import kitchenpos.product.domain.Price;

public class ProductRequest {
    private Name name;
    private Price price;

    public ProductRequest() {
    }

    public ProductRequest(Name name, Price price) {
        this.name = name;
        this.price = price;
    }

    public Name getName() {
        return name;
    }

    public Price getPrice() {
        return price;
    }

    @JsonGetter("name")
    public String name() {
        return name.value();
    }

    @JsonGetter("price")
    public BigDecimal price() {
        return price.value();
    }

    @JsonSetter("price")
    public void setPrice(BigDecimal price) {
        this.price = new Price(price);
    }
}
