package kitchenpos.dto;

import kitchenpos.domain.Price;
import kitchenpos.domain.Product;

import java.math.BigDecimal;

public class ProductResponse {
    private Long id;
    private String name;
    private BigDecimal price;

    public ProductResponse(String name, Price price) {
        this.name = name;
        this.price = price.getPrice();
    }

    public static ProductResponse from(Product savedProduct) {
        return new ProductResponse(savedProduct.getName(), savedProduct.getPrice());
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }
}
