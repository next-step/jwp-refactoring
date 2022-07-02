package kitchenpos.helper;

import kitchenpos.product.dto.ProductRequest;

public class ProductRequestBuilder {

    private String name;
    private int price;

    private ProductRequestBuilder() {
    }

    public static ProductRequestBuilder builder() {
        return new ProductRequestBuilder();
    }

    public ProductRequestBuilder name(String name) {
        this.name = name;
        return this;
    }

    public ProductRequestBuilder price(int price) {
        this.price = price;
        return this;
    }

    public ProductRequest build() {
        return new ProductRequest(name, price);
    }
}
