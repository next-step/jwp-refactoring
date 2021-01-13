package kitchenpos.product.dto;

import kitchenpos.infra.Money;
import kitchenpos.product.domain.Product;

public class ProductRequest {
    private String name;
    private int price;

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
