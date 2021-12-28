package kitchenpos.product.dto;

import kitchenpos.product.domain.Product;

public class ProductRequest {
    private String name;
    private Long price;

    protected ProductRequest() {
    }

    public ProductRequest(String name, Long price) {
        this.name = name;
        this.price = price;
    }

    public static ProductRequest of(String name, Long price) {
        return new ProductRequest(name, price);
    }

    public String getName() {
        return name;
    }

    public Long getPrice() {
        return price;
    }

    public Product toProduct() {
        return Product.of(name, price);
    }
}
