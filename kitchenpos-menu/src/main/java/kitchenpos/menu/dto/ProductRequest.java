package kitchenpos.menu.dto;

import kitchenpos.menu.domain.Product;

public class ProductRequest {
    private String name;
    private Integer price;

    public ProductRequest() {}

    public ProductRequest(String name, Integer price) {
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public Integer getPrice() {
        return price;
    }

    public Product toEntity() {
        return new Product(name, price);
    }
}
