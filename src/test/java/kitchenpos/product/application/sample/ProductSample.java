package kitchenpos.product.application.sample;

import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import kitchenpos.common.domain.Name;
import kitchenpos.common.domain.Price;
import kitchenpos.product.domain.Product;

public class ProductSample {

    public static Product 후라이드치킨() {
        Product product = spy(Product.of(Name.from("후라이드치킨"), Price.from(BigDecimal.ONE)));
        when(product.id()).thenReturn(1L);
        return product;
    }
}
