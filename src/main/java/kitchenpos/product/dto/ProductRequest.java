package kitchenpos.product.dto;

import kitchenpos.product.domain.Product;

public class ProductRequest {
    private String name;
    private Integer price;

    public ProductRequest() {
    }

    public ProductRequest(String name, Integer price) {
        this.name = name;
        this.price = price;
    }

    public ProductRequest(Product product) {
        this.name = product.getName();
        this.price = product.getPriceValue();
    }

    public String getName() {
        return name;
    }

    public Integer getPrice() {
        return price;
    }
}
