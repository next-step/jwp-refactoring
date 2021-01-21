package kitchenpos.product.dto;

import kitchenpos.product.domain.Product;

public class ProductRequest {
	private String name;
	private long price;

	public ProductRequest() {
	}

	public ProductRequest(String name, long price) {
		this.name = name;
		this.price = price;
	}

	public static ProductRequest of(String name, long price) {
		return new ProductRequest(name, price);
	}

	public Product toProduct() {
		return Product.of(name, price);
	}

	public String getName() {
		return this.name;
	}
}
