package kitchenpos.product.dto;

import java.math.BigDecimal;

import kitchenpos.product.domain.domain.Product;

public class ProductAddRequest {

	private String name;
	private BigDecimal price;

	protected ProductAddRequest() {
	}

	private ProductAddRequest(String name, BigDecimal price) {
		this.name = name;
		this.price = price;
	}

	public static ProductAddRequest of(String name, BigDecimal price) {
		return new ProductAddRequest(name, price);
	}

	public String getName() {
		return name;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public Product toEntity() {
		return Product.of(name, price);
	}
}
