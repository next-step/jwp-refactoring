package kitchenpos.application.fixture;

import java.math.BigDecimal;
import kitchenpos.domain.product.Product;

public class ProductFixture {

    private ProductFixture() {
    }


    public static Product 후리이드치킨(Long id) {
        return Product.of(id, "후리이드치킨", 15000);
    }

    public static Product 마이너스_가격_상품() {
        return Product.of("후리이드치킨", -15000);
    }

}
