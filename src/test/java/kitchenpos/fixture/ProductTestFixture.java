package kitchenpos.fixture;

import kitchenpos.domain.Product;
import kitchenpos.dto.ProductRequest;

import java.math.BigDecimal;

public class ProductTestFixture {

    public static ProductRequest 상품(String name, BigDecimal price) {
        return ProductRequest.of(name, price);
    }

    public static ProductRequest 단무지요청() {
        return 상품("단무지", BigDecimal.valueOf(0L));
    }

    public static ProductRequest 짬뽕요청() {
        return 상품("짬뽕", BigDecimal.valueOf(9000L));
    }

    public static ProductRequest 탕수육요청() {
        return 상품("탕수육", BigDecimal.valueOf(12000L));
    }

    public static ProductRequest 짜장면요청() {
        return 상품("짜장면", BigDecimal.valueOf(8000L));
    }

    public static Product 상품생성(ProductRequest request) {
        return Product.of(request.getName(), request.getPrice());
    }
}
