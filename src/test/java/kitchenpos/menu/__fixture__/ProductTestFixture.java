package kitchenpos.menu.__fixture__;

import java.math.BigDecimal;
import kitchenpos.menu.domain.Product;
import kitchenpos.menu.request.ProductRequest;

public class ProductTestFixture {
    public static Product 상품_생성(final Long id, final String name, final BigDecimal price) {
        return new Product(id, name, price);
    }

    public static ProductRequest 상품_요청_생성(final String name, final BigDecimal price) {
        return new ProductRequest(name, price);
    }
}
