package kitchenpos.product.dto;

import kitchenpos.product.domain.Product;

public class ProductRequest {
	private String name;
	private long price;

	public ProductRequest() {
	}

	public ProductRequest(final String name, final long price) {
		this.name = name;
		this.price = price;
	}

	public static ProductRequest of(final String name, final long price) {
		return new ProductRequest(name, price);
	}

	public Product toEntity() {
		return Product.of(name, price);
	}

	public String getName() {
		return this.name;
	}

	public long getPrice() {
		return this.price;
	}
}
