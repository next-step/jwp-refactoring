package kitchenpos.application;

import java.math.BigDecimal;

import kitchenpos.domain.Product;

public class ProductServiceTestHelper {
    private ProductServiceTestHelper() {
    }

    public static Product makeProduct(Long id, String name, BigDecimal price) {
        Product product = new Product();

        product.setId(id);
        product.setName(name);
        product.setPrice(price);

        return product;
    }
}
