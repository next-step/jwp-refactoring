package kitchenpos.product.application;

import java.math.BigDecimal;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.domain.Product;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTestHelper {
    public static Product 상품_정보(Long id, String name, int price) {
        Product product = new Product();
        product.setId(id);
        product.setName(name);
        product.setPrice(BigDecimal.valueOf(price));
        return product;
    }
}
