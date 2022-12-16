package kitchenpos.dto;

import kitchenpos.domain.Product;

import java.math.BigDecimal;

public class ProductRequest {
    private final String name;
    private final BigDecimal price;

    public ProductRequest(final String name, final BigDecimal price) {
        this.name = name;
        this.price = price;
    }

    public static ProductRequest of(final String name, final BigDecimal price) {
        return new ProductRequest(name, price);
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public Product toEntity() {
        return Product.of(name, price);
    }
}


