package kitchenpos.dto;

import kitchenpos.domain.ProductEntity;
import java.math.BigDecimal;

public class ProductRequest {
    private String name;
    private BigDecimal price;

    public ProductRequest(String name, BigDecimal price) {
        this.name = name;
        this.price = price;
    }

    protected ProductRequest() {
    }

    public ProductEntity toProduct() {
        return new ProductEntity(name, price);
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }
}
