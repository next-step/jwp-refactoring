package kitchenpos;

import java.math.BigDecimal;

import kitchenpos.product.domain.Product;

public class TestFixtures {
	public static Product 상품1 = newProduct(1L, 16000L, "후라이드");
	public static Product 상품2 = newProduct(2L, 16000L, "양념치킨");
	public static Product 상품3 = newProduct(3L, 16000L, "반반치킨");
	public static Product 상품4 = newProduct(4L, 16000L, "통구이");
	public static Product 상품5 = newProduct(5L, 17000L, "간장치킨");
	public static Product 상품6 = newProduct(6L, 17000L, "순살치킨");

	private static Product newProduct(Long id, Long price, String name) {
		return new Product.Builder().id(id).price(BigDecimal.valueOf(price)).name(name).build();
	}
}
