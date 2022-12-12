package kitchenpos.product.dto;

import kitchenpos.product.domain.Product;

import java.math.BigDecimal;

public class ProductCreateRequest {

    private String name;
    private BigDecimal price;

    public ProductCreateRequest(BigDecimal price) {
        this.price = price;
    }

    public Product toProduct() {
        return new Product(this.name, this.price);
    }

    public BigDecimal getPrice() {
        return this.price;
    }
}
