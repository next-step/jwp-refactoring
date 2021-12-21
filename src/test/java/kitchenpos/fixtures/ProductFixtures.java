package kitchenpos.fixtures;

import kitchenpos.domain.Product;
import kitchenpos.dto.ProductRequest;

import java.math.BigDecimal;

/**
 * packageName : kitchenpos.fixtures
 * fileName : ProductFixtures
 * author : haedoang
 * date : 2021/12/17
 * description :
 */
public class ProductFixtures {
    public static ProductRequest 양념치킨요청() {
        return ProductRequest.of("양념치킨", new BigDecimal(16000));
    }

    public static Product 양념치킨() {
        return 양념치킨요청().toEntity();
    }

    public static ProductRequest 후라이드요청() {
        return ProductRequest.of("후라이드", new BigDecimal(16000));
    }

    public static Product 후라이드() {
        return 후라이드요청().toEntity();
    }
}
