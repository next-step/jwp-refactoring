package kitchenpos.product;

import kitchenpos.domain.Product;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;

public class ProductTestSupport {

    /**
     * 새로운 상품을 만듭니다.
     * @param name
     * @param price
     * @return 상품
     */
    public static Product createProduct(String name, BigDecimal price) {
        Product product = new Product();
        ReflectionTestUtils.setField(product, "name", name);
        ReflectionTestUtils.setField(product, "price", price);

        return product;
    }
}
