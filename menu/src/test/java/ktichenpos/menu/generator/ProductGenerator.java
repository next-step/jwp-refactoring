package ktichenpos.menu.generator;

import static org.mockito.Mockito.*;

import java.math.BigDecimal;

import ktichenpos.menu.menu.domain.Price;
import ktichenpos.menu.menu.domain.Product;

public class ProductGenerator {

	private ProductGenerator() {
	}

	public static Product 상품(String name) {
		return Product.of(name, Price.from(BigDecimal.TEN));
	}

	public static Product 상품(String name, BigDecimal price) {
		return Product.of(name, Price.from(price));
	}

	public static Product 후라이드_치킨() {
		Product 후라이드 = spy(Product.of("후라이드", Price.from(BigDecimal.TEN)));
		lenient().when(후라이드.id()).thenReturn(1L);
		return 후라이드;
	}

	public static Product 양념_치킨() {
		Product 양념 = spy(Product.of("양념", Price.from(BigDecimal.TEN)));
		lenient().when(양념.id()).thenReturn(2L);
		return 양념;
	}
}
