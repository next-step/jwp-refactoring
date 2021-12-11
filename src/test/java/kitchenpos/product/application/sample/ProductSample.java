package kitchenpos.product.application.sample;

import java.math.BigDecimal;
import kitchenpos.common.domain.Name;
import kitchenpos.common.domain.Price;
import kitchenpos.product.domain.Product;

public class ProductSample {

    public static Product 후라이드치킨() {
        Product product = new Product();
        product.setId(1L);
        product.setName(Name.from("후라이드치킨"));
        product.setPrice(Price.from(BigDecimal.ONE));
        return product;
    }
}
