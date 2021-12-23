package kitchenpos.product.product.sample;

import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.spy;

import java.math.BigDecimal;
import kichenpos.common.domain.Name;
import kichenpos.common.domain.Price;
import kitchenpos.product.product.domain.Product;

public class ProductSample {

    public static Product 십원치킨() {
        Product product = spy(Product.of(Name.from("치킨"), Price.from(BigDecimal.TEN)));
        lenient().when(product.id())
            .thenReturn(1L);
        return product;
    }
}
