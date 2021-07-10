package kitchenpos.product.dto;

import kitchenpos.product.domain.Product;

import java.math.BigDecimal;
import java.util.ArrayList;

public class ProductResponse {
    private Long id;
    private String name;
    private BigDecimal price;

    public ProductResponse() {}

    public ProductResponse(Product product) {
        this.id = product.getId();
        this.name = product.getName();
        this.price = product.getPrice();
    }

    public static ProductResponse of(Product product) {
        return new ProductResponse(product);
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
