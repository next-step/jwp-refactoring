package kitchenpos.product.dto;

import kitchenpos.domain.Price;
import kitchenpos.product.domain.Product;

public class ProductRequest {
    private String name;
    private Price price;

    public ProductRequest() {
    }

    public ProductRequest(String name, Price price) {
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public Price getPrice() {
        return price;
    }

    public Product toProduct() {
        return new Product(name, price);
    }
}
