package kitchenpos.product.domain;

import kitchenpos.product.domain.Price;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductName;

import java.math.BigDecimal;

public class ProductTestFixture {

    public static Product 상품(String name) {
        return 상품(name, BigDecimal.ONE);
    }

    public static Product 상품(String name, BigDecimal price) {
        return Product.of(ProductName.from(name), Price.from(price));
    }

    public static Product 상품(Long id, String name, BigDecimal price) {
        return Product.of(id, ProductName.from(name), Price.from(price));
    }

    public static Product 상품_통다리() {
        return 상품("통다리", BigDecimal.ONE);
    }

    public static Product 상품_통다리(Long id) {
        return 상품(id, "통다리", BigDecimal.ONE);
    }

    public static Product 상품_콜라() {
        return 상품("콜라", BigDecimal.ONE);
    }

    public static Product 상품_콜라(Long id) {
        return 상품(id, "콜라", BigDecimal.ONE);
    }
}
