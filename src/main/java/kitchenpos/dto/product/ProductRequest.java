package kitchenpos.dto.product;

import kitchenpos.domain.product.Product;

import java.math.BigDecimal;

public class ProductRequest {
    private final String name;
    private final BigDecimal price;

    public ProductRequest(String name, BigDecimal price) {
        this.name = name;
        this.price = price;
    }

    public static ProductRequest of(Product product) {
        return new ProductRequest(product.getName(), product.getPrice());
    }

    public Product toProduct() {
        return new Product(name, price);
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }
}
