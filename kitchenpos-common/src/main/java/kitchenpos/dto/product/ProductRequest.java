package kitchenpos.dto.product;

import kitchenpos.domain.product.Product;

public class ProductRequest {

    private String name;

    private Integer price;

    public ProductRequest() {
    }

    public ProductRequest(String name, Integer price) {
        this.name = name;
        this.price = price;
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
