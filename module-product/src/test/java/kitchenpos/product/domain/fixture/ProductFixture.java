package kitchenpos.product.domain.fixture;

import kitchenpos.product.domain.Name;
import kitchenpos.product.domain.Price;
import kitchenpos.product.domain.Product;

import java.math.BigDecimal;

public class ProductFixture {

    public static Product product() {
        return new Product(new Name("A"), new Price(BigDecimal.ONE));
    }
}
