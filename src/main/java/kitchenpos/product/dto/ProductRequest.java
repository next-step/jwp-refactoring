package kitchenpos.product.dto;

import kitchenpos.menu.domain.Price;
import kitchenpos.product.domain.Product;

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

    public Product toProduct() {
        return new Product(this.name, Price.valueOf(this.price));
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }
}
