package kitchenpos.domain;

import java.math.BigDecimal;

public class ProductTest {

    public static final Product 치킨 = new Product(1L, "치킨", BigDecimal.valueOf(7000));
    public static final Product 피자 = new Product(2L, "피자", BigDecimal.valueOf(9900));
    public static final Product 떡볶이 = new Product(3L, "떡볶이", BigDecimal.valueOf(1000));
    public static final Product 소주 = new Product(4L, "소주", BigDecimal.valueOf(1000));
    public static final Product 맥주 = new Product(5L, "맥주", BigDecimal.valueOf(2000));

}
