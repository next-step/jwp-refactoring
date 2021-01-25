package kitchenpos.dto.product;

import kitchenpos.domain.Price;
import kitchenpos.domain.product.Product;

public class ProductRequest {
    private String name;
    private int price;

    public ProductRequest() {
    }

    public ProductRequest(String name, int price) {
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public Product toProduct() {
        return new Product(name, new Price(price));
    }
}
