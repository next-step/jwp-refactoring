package kitchenpos;

import kitchenpos.product.dto.ProductRequest;

import java.math.BigDecimal;

public class TestProductRequestFactory {
    public static ProductRequest create(String name, int price) {
        return create(name, BigDecimal.valueOf(price));
    }

    public static ProductRequest create(String name, BigDecimal price) {
        return new ProductRequest(name, price);
    }
}
