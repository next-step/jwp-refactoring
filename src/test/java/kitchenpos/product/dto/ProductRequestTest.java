package kitchenpos.product.dto;

import java.math.BigDecimal;
import kitchenpos.menu.dto.ProductRequest;

public class ProductRequestTest {

    public static ProductRequest 상품_요청_객체_생성(String name, BigDecimal price) {
        return new ProductRequest.Builder()
                .name(name)
                .price(price)
                .build();
    }
}