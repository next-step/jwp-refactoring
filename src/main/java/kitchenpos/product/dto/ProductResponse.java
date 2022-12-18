package kitchenpos.product.dto;

import kitchenpos.common.Name;
import kitchenpos.product.domain.Product;

import java.math.BigDecimal;

public class ProductResponse {

    private final Long id;
    private final Name name;
    private final BigDecimal price;

    public ProductResponse(Long id, Name name, BigDecimal price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public static ProductResponse of(Product product) {
        return new ProductResponse(product.getId(), product.getName(), product.getPrice());
    }

    public Long getId() {
        return this.id;
    }

    public BigDecimal getPrice() {
        return this.price;
    }

    public Name getName() {
        return this.name;
    }
}
