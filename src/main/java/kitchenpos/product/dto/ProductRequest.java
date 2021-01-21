package kitchenpos.product.dto;

import kitchenpos.product.domain.Product;

public class ProductRequest {
	private final String name;
	private final long price;

	public ProductRequest(String name, long price) {
		this.name = name;
		this.price = price;
	}

	public static ProductRequest of(String name, long price) {
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
