package kitchenpos.application.sample;

import java.math.BigDecimal;
import kitchenpos.domain.Product;

public class ProductSample {

    public static Product 짜장면() {
        Product product = new Product();
        product.setId(1L);
        product.setName("짜장면");
        product.setPrice(BigDecimal.ONE);
        return product;
    }

}
