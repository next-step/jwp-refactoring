package kitchenpos.product.dto;

import kitchenpos.product.domain.Product;

public class ProductRequest {

    private final String name;
    private final Integer price;

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
        return Product.of(name, price);
    }
}
