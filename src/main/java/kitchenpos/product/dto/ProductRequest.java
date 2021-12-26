package kitchenpos.product.dto;

import kitchenpos.product.domain.Product;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

public class ProductRequest {

    @NotNull
    private String name;

    @NotNull
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

    public Product toEntity() {
        return Product.create(name, price);
    }
}
