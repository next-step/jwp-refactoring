package kitchenpos.product.dto;

import kitchenpos.product.domain.Product;

public class ProductRequest {
    private String name;
    private Integer price;

    protected ProductRequest() {
    }

    protected ProductRequest(String name, Integer price) {
        this.name = name;
        this.price = price;
    }

    protected ProductRequest(Product product) {
        this.name = product.getName();
        this.price = product.getPriceValue();
    }

    public static ProductRequest from(Product product) {
        return new ProductRequest(product);
    }

    public static ProductRequest from(String name, Integer price) {
        return new ProductRequest(name, price);
    }

    public String getName() {
        return name;
    }

    public Integer getPrice() {
        return price;
    }
}
