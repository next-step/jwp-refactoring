package kitchenpos.product.dto;

import kitchenpos.generic.Money;
import kitchenpos.product.domain.Product;

public class ProductRequest {
    private final String name;
    private final int price;

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

    public static Product toProduct(ProductRequest request) {
        return new Product(request.name, Money.price(request.price));
    }
}
