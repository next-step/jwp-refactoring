package kitchenpos.product.dto;

import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductPrice;

public class ProductRequest {
    private final String name;
    private final Integer price;

    public ProductRequest(String name, Integer price) {
        this.name = name;
        this.price = price;
    }

    public Product toProduct(){
        return new Product(name, new ProductPrice(price));
    }

    public String getName() {
        return name;
    }

    public Integer getPrice() {
        return price;
    }
}
