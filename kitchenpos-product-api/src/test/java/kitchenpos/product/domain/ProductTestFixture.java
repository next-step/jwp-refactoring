package kitchenpos.product.domain;

import java.math.BigDecimal;
import kitchenpos.common.domain.Name;
import kitchenpos.common.domain.Price;
import kitchenpos.product.dto.ProductRequest;

public class ProductTestFixture {

    public static Product generateProduct(Long id, String name, BigDecimal price) {
        return new Product(id, name, price);
    }

    public static Product generateProduct(String name, BigDecimal price) {
        return new Product(null, name, price);
    }

    public static ProductRequest generateProductRequest(String name, BigDecimal price) {
        return new ProductRequest(name, price);
    }

    public static ProductRequest generateProductRequest(Name name, Price price) {
        return new ProductRequest(name.value(), price.value());
    }
}
