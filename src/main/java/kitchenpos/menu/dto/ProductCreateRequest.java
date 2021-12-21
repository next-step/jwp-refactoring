package kitchenpos.menu.dto;

import kitchenpos.menu.domain.Product;

import java.math.BigDecimal;

public class ProductCreateRequest {
    private final BigDecimal price;
    private final String name;

    public ProductCreateRequest(String name, BigDecimal price) {
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
