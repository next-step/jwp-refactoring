package kitchenpos.product.dto;

import java.math.BigDecimal;
import kitchenpos.product.domain.Product;

public class ProductRequest {
    private final String name;
    private final BigDecimal price;

    public ProductRequest(String name, Integer price) {
        this.name = name;
        this.price = BigDecimal.valueOf(price);
    }

    public Product toProduct() {
        return new Product(name, price);
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }
}
