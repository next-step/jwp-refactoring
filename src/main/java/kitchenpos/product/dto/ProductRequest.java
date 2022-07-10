package kitchenpos.product.dto;

import kitchenpos.product.domain.Price;
import kitchenpos.product.domain.Product;

public class ProductRequest {
    private String name;
    private Integer price;

    public ProductRequest() {
    }

    public ProductRequest(Product product) {
        this(product.getName(), product.getPriceValue());
    }

    public ProductRequest(String name, Integer price) {
        this.name = name;
        this.price = price;
    }

    public Product toEntity() {
        return new Product(name, Price.from(price));
    }

    public String getName() {
        return name;
    }

    public Integer getPrice() {
        return price;
    }
}
