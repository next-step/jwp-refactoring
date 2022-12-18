package kitchenpos.generator;

import java.math.BigDecimal;

import kitchenpos.domain.Product;

public class ProductGenerator {

	private ProductGenerator() {
	}

	public static Product 상품(String name) {
		return new Product(1L, name, BigDecimal.ONE);
	}

	public static Product 상품(String name, BigDecimal price) {
		return new Product(1L, name, price);
	}
}
