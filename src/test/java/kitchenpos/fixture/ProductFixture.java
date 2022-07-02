package kitchenpos.fixture;

import java.math.BigDecimal;
import kitchenpos.product.domain.Product;
import kitchenpos.product.dto.ProductRequest;

public class ProductFixture {
    public static Product 상품_생성(Long id, String name, int price) {
        return new Product(id, name, BigDecimal.valueOf(price));
    }

    public static ProductRequest 상품_요청_생성(String name, int price) {
        return new ProductRequest(name, price);
    }
}
