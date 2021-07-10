package kitchenpos.dto.product;

import kitchenpos.domain.product.Price;
import kitchenpos.domain.product.Product;

public class ProductRequest {

    private final String name;
    private final Price price;

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

    public Product toEntity() {
        return Product.of(name, price);
    }
}
