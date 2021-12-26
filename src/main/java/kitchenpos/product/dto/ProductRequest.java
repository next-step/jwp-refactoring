package kitchenpos.product.dto;

import kitchenpos.product.domain.Product;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;

public class ProductRequest {

    @NotBlank
    private String name;

    @Positive
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
