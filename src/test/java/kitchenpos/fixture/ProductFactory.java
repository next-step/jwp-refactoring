package kitchenpos.fixture;

import java.math.BigDecimal;
import kitchenpos.dto.ProductRequest;
import kitchenpos.dto.ProductResponse;

public class ProductFactory {
    public static ProductRequest createProductRequest(String name, BigDecimal price) {
        return new ProductRequest(name, price);
    }

    public static ProductResponse createProductResponse(Long id, String name, BigDecimal price) {
        return new ProductResponse(id, name, price);
    }
}
