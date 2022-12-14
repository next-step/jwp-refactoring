package kitchenpos.product.domain;

import java.math.BigDecimal;
import kitchenpos.product.dto.ProductRequest;

public class ProductTestFixture {
    public static final ProductRequest 짜장면_요청 = productRequest("짜장면", BigDecimal.valueOf(7_000L));
    public static final ProductRequest 짬뽕_요청 = productRequest("짬뽕", BigDecimal.valueOf(8_000L));
    public static final ProductRequest 탕수육_소_요청 = productRequest("탕수육_소", BigDecimal.valueOf(18_000L));
    public static final Product 짜장면 = product(1L, "짜장면", BigDecimal.valueOf(7_000L));
    public static final Product 짬뽕 = product(2L, "짬뽕", BigDecimal.valueOf(8_000L));
    public static final Product 탕수육_소 = product(3L, "탕수육_소", BigDecimal.valueOf(18_000L));

    public static ProductRequest productRequest(String name, BigDecimal price) {
        return new ProductRequest(name, price);
    }

    public static Product product(Long id, String name, BigDecimal price) {
        return Product.of(id, name, price);
    }

    public static Product product(String name, BigDecimal price) {
        return Product.of(name, price);
    }
}
