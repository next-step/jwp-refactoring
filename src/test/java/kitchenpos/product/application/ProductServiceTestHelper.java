package kitchenpos.product.application;

import java.math.BigDecimal;

import kitchenpos.domain.Product;

public class ProductServiceTestHelper {

    public static Product 상품_정보(Long id, String name, int price) {
        Product product = new Product();
        product.setId(id);
        product.setName(name);
        product.setPrice(BigDecimal.valueOf(price));
        return product;
    }
}
