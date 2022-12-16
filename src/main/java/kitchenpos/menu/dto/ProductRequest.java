package kitchenpos.menu.dto;

import kitchenpos.menu.domain.Price;
import kitchenpos.menu.domain.Product;

import java.math.BigDecimal;

public class ProductRequest {
    private String name;
    private BigDecimal price;

    public ProductRequest() {}

    public ProductRequest(String name, BigDecimal price) {
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public Product toEntity() {
        return new Product(name, new Price(price));
    }
}
