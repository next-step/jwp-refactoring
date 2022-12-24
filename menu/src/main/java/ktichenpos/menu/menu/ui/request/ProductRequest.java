package ktichenpos.menu.menu.ui.request;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import kitchenpos.menu.domain.Price;
import kitchenpos.menu.domain.Product;

public class ProductRequest {

	private final String name;
	private final BigDecimal price;

	@JsonCreator
	public ProductRequest(
		@JsonProperty("name") String name,
		@JsonProperty("price") BigDecimal price
	) {
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
