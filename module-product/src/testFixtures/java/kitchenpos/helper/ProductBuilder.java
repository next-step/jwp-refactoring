package kitchenpos.helper;

import kitchenpos.common.domain.Price;
import kitchenpos.product.domain.Product;

public class ProductBuilder {

    private Long id;
    private String name;
    private int price;

    private ProductBuilder() {
    }

    public static ProductBuilder builder() {
        return new ProductBuilder();
    }

    public ProductBuilder id(Long id) {
        this.id = id;
        return this;
    }

    public ProductBuilder name(String name) {
        this.name = name;
        return this;
    }

    public ProductBuilder price(int price) {
        this.price = price;
        return this;
    }

    public Product build() {
        return new Product(id, name, new Price(price));
    }
}
