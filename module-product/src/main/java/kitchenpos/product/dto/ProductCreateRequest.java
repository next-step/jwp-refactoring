package kitchenpos.product.dto;

import kitchenpos.domain.Price;
import kitchenpos.product.domain.Product;

import java.math.BigDecimal;

public class ProductCreateRequest {
    private String name;
    private BigDecimal price;

    public Product of() {
        return new Product(name, new Price(price));
    }

    protected ProductCreateRequest() {}

    public ProductCreateRequest(String name, BigDecimal price) {
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }
}
