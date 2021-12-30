package kitchenpos.dto.product;

import kitchenpos.domain.Product;

public class ProductRequest {
    private String name;
    private long price;

    protected ProductRequest() {
    }

    private ProductRequest(final String name, final long price) {
        this.name = name;
        this.price = price;
    }

    public static ProductRequest of(final String name, final long price) {
        return new ProductRequest(name, price);
    }

    public Product toProduct() {
        return Product.of(name, price);
    }

    public String getName() {
        return name;
    }

    public long getPrice() {
        return price;
    }
}
