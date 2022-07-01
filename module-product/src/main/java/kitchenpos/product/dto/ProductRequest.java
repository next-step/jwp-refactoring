package kitchenpos.product.dto;

import kitchenpos.product.domain.Product;

public class ProductRequest {
    private String name;

    private Integer price;

    protected ProductRequest() {
    }

    private ProductRequest(String name, Integer price) {
        this.name = name;
        this.price = price;
    }

    public static ProductRequest of(String name, Integer price) {
        return new ProductRequest(name, price);
    }

    public Product toEntity() {
        return Product.of(this.name, this.price);
    }

    public String getName() {
        return name;
    }

    public Integer getPrice() {
        return price;
    }
}
