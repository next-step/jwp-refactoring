package kitchenpos.domain;

import java.math.BigDecimal;

public class ProductTest {

	public static final Product 후라이드 = new Product(1L, "후라이드", new BigDecimal(10_000));
	public static final Product 양념치킨 = new Product(2L, "양념치킨", new BigDecimal(11_000));
	public static final Product 간장치킨 = new Product(3L, "간장치킨", new BigDecimal(12_000));

}
