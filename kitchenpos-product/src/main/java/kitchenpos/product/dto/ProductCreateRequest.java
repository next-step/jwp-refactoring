package kitchenpos.product.dto;

import kitchenpos.global.domain.Price;
import kitchenpos.product.domain.Product;

import java.math.BigDecimal;

public class ProductCreateRequest {

    private final String name;
    private final BigDecimal price;

    public ProductCreateRequest(String name, BigDecimal price) {
        this.name = name;
        this.price = price;
    }

    public static ProductCreateRequest of(String name, Long price) {
        return new ProductCreateRequest(name, BigDecimal.valueOf(price));
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public Product toProduct() {
        return new Product(this.name, Price.of(price));
    }
}
