package kitchenpos.dto.product;


import kitchenpos.application.product.Product;
import kitchenpos.application.valueobject.Price;

import java.math.BigDecimal;

public class ProductRequest {
    private String name;
    private BigDecimal price;

    public ProductRequest() {
    }

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
    public Product toProduct() {
        return new Product(name, Price.of(price));
    }
}