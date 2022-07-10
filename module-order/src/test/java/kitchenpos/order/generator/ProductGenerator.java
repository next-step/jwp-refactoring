package kitchenpos.order.generator;

import kitchenpos.domain.Price;
import kitchenpos.product.domain.Product;

public class ProductGenerator {

    private ProductGenerator() {}

    public static Product 상품_생성(String name, Price price) {
        return new Product(name, price);
    }
}
