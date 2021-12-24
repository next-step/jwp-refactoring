package kitchenpos.menu.dto;

import java.math.BigDecimal;

import kitchenpos.menu.domain.Product;

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
    
    public static ProductRequest from(Product product) {
        return new ProductRequest(product.getName(), product.getPrice());
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
