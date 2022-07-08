package kitchenpos.testfixture;

import kitchenpos.product.domain.*;
import kitchenpos.product.dto.ProductRequest;

import java.math.BigDecimal;

public class ProductTestFixture {
    public static Product createProduct(String name, BigDecimal price) {
        return new Product(name, price);
    }

    public static ProductRequest createProductRequest(String name, BigDecimal price) {
        return new ProductRequest(name, price);
    }
}
