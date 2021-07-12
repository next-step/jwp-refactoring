package kitchenpos.product.dto;

import java.math.BigDecimal;

import kitchenpos.menu.domain.Price;
import kitchenpos.product.domain.Product;

public class ProductRequest {

	private String name;
	private BigDecimal price;

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

	public Product toProduct() {
		return new Product(name, new Price(price));
	}
}
