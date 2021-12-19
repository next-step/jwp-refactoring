package kitchenpos.domain.product.dto;

import kitchenpos.domain.product.domain.Product;

public class ProductRequest {

    private String name;
    private int price;

    public static ProductRequest of(String name, int price) {
        return new ProductRequest(name, price);
    }

    private ProductRequest(String name, int price) {
        this.name = name;
        this.price = price;
    }

    public Product toProduct() {
        return new Product(name, price);
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }
}
