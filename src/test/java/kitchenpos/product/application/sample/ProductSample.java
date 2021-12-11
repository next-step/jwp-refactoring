package kitchenpos.product.application.sample;

import java.math.BigDecimal;
import kitchenpos.product.domain.Product;

public class ProductSample {

    public static Product 후라이드치킨() {
        Product product = new Product();
        product.setId(1L);
        product.setName("후라이드치킨");
        product.setPrice(BigDecimal.ONE);
        return product;
    }
}
