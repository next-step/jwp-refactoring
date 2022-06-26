package kitchenpos.product.dto;

import kitchenpos.common.domain.Price;
import kitchenpos.product.domain.Product;

public class ProductRequest {
    private final String name;
    private final Integer price;

    public ProductRequest(String name, Integer price) {
        this.name = name;
        this.price = price;
    }

    public Product toProduct(){
        return new Product(name, new Price(price));
    }

    public String getName() {
        return name;
    }

    public Integer getPrice() {
        return price;
    }
}
