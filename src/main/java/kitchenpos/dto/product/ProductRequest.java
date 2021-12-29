package kitchenpos.dto.product;

import kitchenpos.domain.Product;

import java.math.BigDecimal;

public class ProductRequest {
    private String name;
    private BigDecimal price;

    protected ProductRequest() {
    }

    private ProductRequest(final String name, final BigDecimal price) {
        this.name = name;
        this.price = price;
    }

    public static ProductRequest of(final String name, final BigDecimal price) {
        return new ProductRequest(name, price);
    }

    public Product toProduct() {
        return Product.of(name, price);
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }
}
