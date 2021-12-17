package common;

import java.math.BigDecimal;
import kitchenpos.domain.Product;

public class ProductFixture {

    public static Product 후라이드() {
        return new Product(1L, "후라이드", new BigDecimal(16000));
    }

    public static Product 양념치킨() {
        return new Product(2L, "양념치킨", new BigDecimal(16000));
    }

    public static Product 가격이없는_반반치킨() {
        return new Product(3L, "반반치킨", new BigDecimal(-1));
    }

    public static Product 반반치킨() {
        return new Product(4L, "반반치킨", new BigDecimal(16500));
    }

}
