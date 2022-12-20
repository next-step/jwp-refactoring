package product.dto;

import product.domain.Product;

import java.math.BigDecimal;

public class ProductRequest {
    private String name;
    private BigDecimal price;

    private ProductRequest() {
    }

    private ProductRequest(String name, BigDecimal price) {
        this.name = name;
        this.price = price;
    }

    public static ProductRequest of(String name, BigDecimal price) {
        return new ProductRequest(name, price);
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public Product toProduct() {
        return Product.of(name, price);
    }
}
