package kitchenpos.menu.dto;

import kitchenpos.menu.domain.product.Product;

import java.math.BigDecimal;

public class ProductRequest {

    private String name;
    private BigDecimal productPrice;

    public ProductRequest() {
    }

    private ProductRequest(String name, BigDecimal productPrice) {
        this.name = name;
        this.productPrice = productPrice;
    }

    public static ProductRequest of(String name, BigDecimal productPrice) {
        return new ProductRequest(name, productPrice);
    }

    public String getName() {
        return name;
    }

    public BigDecimal getProductPrice() {
        return productPrice;
    }

    public Product toProduct() {
        return new Product(name, productPrice);
    }
}
