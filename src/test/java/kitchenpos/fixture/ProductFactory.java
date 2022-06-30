package kitchenpos.fixture;

import java.math.BigDecimal;
import kitchenpos.domain.Price;
import kitchenpos.domain.Product;
import kitchenpos.domain.ProductName;
import kitchenpos.dto.ProductRequest;
import kitchenpos.dto.ProductResponse;

public class ProductFactory {
    public static Product createProduct(Long id, String name, long price) {
        Product product = new Product(id, ProductName.from(name), Price.from(price));

        return product;
    }

    public static ProductRequest createProductRequest(String name, BigDecimal price) {
        return new ProductRequest(name, price);
    }

    public static ProductResponse createProductResponse(Long id, String name, BigDecimal price) {
        return new ProductResponse(id, name, price);
    }
}
