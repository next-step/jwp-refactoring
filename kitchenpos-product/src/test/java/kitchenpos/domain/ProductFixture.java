package kitchenpos.domain;

import org.springframework.test.util.ReflectionTestUtils;

public class ProductFixture {

    public static Product 상품(Long id, String name, int price) {
        Product product = new Product(name, price);
        ReflectionTestUtils.setField(product, "id", id);
        return product;
    }

}
