package kitchenpos.product.sample;

import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.spy;

import java.math.BigDecimal;
import kitchenpos.common.domain.Name;
import kitchenpos.common.domain.Price;
import kitchenpos.product.domain.Product;

public class ProductSample {

    public static Product 십원치킨() {
        Product product = spy(Product.of(Name.from("치킨"), Price.from(BigDecimal.TEN)));
        lenient().when(product.id())
            .thenReturn(1L);
        return product;
    }
}
