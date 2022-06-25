package kitchenpos.product.dto;

import kitchenpos.product.domain.Product;

public class ProductRequest {
    private String name;
    private Long price;

    public ProductRequest() {
    }

    public ProductRequest(String name, Long price) {
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public Long getPrice() {
        return price;
    }

    public Product toProduct() {
        return new Product(null, this.name, this.price);
    }
}
