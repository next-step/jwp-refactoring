package kitchenpos.product.domain;

import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;

public class ProductFixture {

    public static Product 상품(Long id, String name, BigDecimal price) {
        Product product = new Product(name, price);
        ReflectionTestUtils.setField(product, "id", id);
        return product;
    }

}
