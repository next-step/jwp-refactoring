package kitchenpos.dto;

import kitchenpos.domain.Product;

import java.math.BigDecimal;

public class ProductRequest {
    private String name;
    private int price;

    protected ProductRequest() {
    }

    private ProductRequest(String name, int price) {
        this.name = name;
        this.price = price;
    }

    public static ProductRequest of(String name, int price) {
        return new ProductRequest(name, price);
    }

    public static ProductRequest from(Product product) {
        return new ProductRequest(product.getName(), product.getPrice().intValue());
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public Product toProduct() {
        return new Product(name, BigDecimal.valueOf(price));
    }
}
