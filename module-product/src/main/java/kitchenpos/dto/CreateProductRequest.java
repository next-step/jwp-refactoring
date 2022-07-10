package kitchenpos.dto;

import kitchenpos.domain.Product;

import java.math.BigDecimal;

public class CreateProductRequest {
    private final String name;
    private final long price;

    public CreateProductRequest(final String name, final long price) {
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public long getPrice() {
        return price;
    }

    public Product toEntity() {
        return Product.of(name, BigDecimal.valueOf(price));
    }
}
