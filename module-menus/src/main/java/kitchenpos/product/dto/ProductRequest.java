package kitchenpos.product.dto;

import java.math.BigDecimal;

import kitchenpos.generic.price.domain.Price;
import kitchenpos.product.domain.Product;

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

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Product toEntity() {
        return new Product(name, Price.valueOf(price));
    }
}
