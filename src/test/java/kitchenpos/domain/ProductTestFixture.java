package kitchenpos.domain;

import java.math.BigDecimal;
import kitchenpos.dto.ProductRequest;

public class ProductTestFixture {

    public static Product generateProduct(Long id, String name, BigDecimal price) {
        return Product.of(id, name, price);
    }

    public static Product generateProduct(String name, BigDecimal price) {
        return Product.of(null, name, price);
    }

    public static ProductRequest generateProductRequest(String name, BigDecimal price) {
        return new ProductRequest(name, price);
    }

    public static ProductRequest generateProductRequest(Name name, Price price) {
        return new ProductRequest(name.value(), price.value());
    }
}
