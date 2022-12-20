package kitchenpos.menu.ui.request;

import java.math.BigDecimal;

import kitchenpos.menu.domain.Price;
import kitchenpos.menu.domain.Product;

public class ProductRequest {

	private final String name;
	private final BigDecimal price;

	public ProductRequest(String name, BigDecimal price) {
		this.name = name;
		this.price = price;
	}

	public String getName() {
		return name;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public Product toEntity() {
		return Product.of(name, Price.from(price));
	}
}
