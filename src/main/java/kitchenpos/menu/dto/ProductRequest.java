package kitchenpos.menu.dto;

import java.math.BigDecimal;

import kitchenpos.menu.domain.Product;

public class ProductRequest {

	private String name;
	private BigDecimal price;

	private ProductRequest(String name, BigDecimal price) {
		this.name = name;
		this.price = price;
	}

	public static ProductRequest of(String name, BigDecimal price) {
		return new ProductRequest(name, price);
	}

	public Product toEntity() {
		return Product.of(name, price);
	}

	public String getName() {
		return name;
	}

	public BigDecimal getPrice() {
		return price;
	}
}
