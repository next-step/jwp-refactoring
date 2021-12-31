package kitchenpos.application.product.dto;

import kitchenpos.product.Product;

import java.math.BigDecimal;

public class ProductRequest {
    private final BigDecimal price;
    private final String name;

    public ProductRequest(String name, BigDecimal price) {
        this.price = price;
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public String getName() {
        return name;
    }

    public Product toEntity() {
        return Product.of(name, price);
    }
}
