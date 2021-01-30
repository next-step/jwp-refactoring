package kitchenpos.menu.dto;

import kitchenpos.menu.domain.Product;

public class ProductRequest {
	private String name;
	private Integer price;

	protected ProductRequest() {
	}

	public ProductRequest(String name, Integer price) {
		this.name = name;
		this.price = price;
	}

	public String getName() {
		return name;
	}

	public int getPrice() {
		return price;
	}

	public Product toProduct() {
		return Product.builder().name(name).price(price).build();
	}
}
