package kitchenpos.product;

import java.math.BigDecimal;

import kitchenpos.common.domain.Name;
import kitchenpos.common.domain.Price;
import kitchenpos.product.domain.Product;

public class ProductFixture {
	public static Product 후라이드치킨_상품() {
		return Product.of(1L, Name.from("후라이드치킨"), Price.from(BigDecimal.valueOf(16000L)));
	}

	public static Product 강정치킨_상품() {
		return Product.of(2L, Name.from("강정치킨"), Price.from(BigDecimal.valueOf(17000L)));
	}

	public static Product 양념치킨_상품() {
		return Product.of(3L, Name.from("양념치킨"), Price.from(BigDecimal.valueOf(18000L)));
	}
}
