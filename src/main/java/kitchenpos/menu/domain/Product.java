package kitchenpos.menu.domain;

import java.math.BigDecimal;

import kitchenpos.common.domain.Price;

public class Product {
	private Price price;

	private Product() {
	}

	public static Product from(BigDecimal price) {
		Product product = new Product();
		product.price = Price.from(price);
		return product;
	}

	public Price getPrice() {
		return price;
	}
}
