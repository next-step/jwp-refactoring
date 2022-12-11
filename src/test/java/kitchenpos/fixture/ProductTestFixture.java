package kitchenpos.fixture;

import kitchenpos.domain.Product;
import kitchenpos.dto.ProductRequest;

import java.math.BigDecimal;

public class ProductTestFixture {

    public static ProductRequest createProduct(Long id, String name, BigDecimal price) {
        return ProductRequest.of(id, name, price);
    }

    public static ProductRequest createProduct(String name, BigDecimal price) {
        return ProductRequest.of(null, name, price);
    }

    public static ProductRequest 단무지_요청() {
        return createProduct(4L, "단무지", BigDecimal.valueOf(0L));
    }

    public static ProductRequest 짬뽕_요청() {
        return createProduct(3L, "짬뽕", BigDecimal.valueOf(9000L));
    }

    public static ProductRequest 탕수육_요청() {
        return createProduct(2L, "탕수육", BigDecimal.valueOf(12000L));
    }

    public static ProductRequest 짜장면_요청() {
        return createProduct(1L, "짜장면", BigDecimal.valueOf(8000L));
    }

    public static Product 상품생성(ProductRequest request) {
        return Product.of(request.getId(), request.getName(), request.getPrice());
    }
}
