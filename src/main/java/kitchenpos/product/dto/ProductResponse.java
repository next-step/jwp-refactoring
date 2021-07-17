package kitchenpos.product.dto;

import kitchenpos.menu.domain.Price;
import kitchenpos.product.domain.Product;

public class ProductResponse {
	private Long id;
	private String name;
	private Price price;

	public ProductResponse() {
	}

	public ProductResponse(Long id, String name, Price price) {
		this.id = id;
		this.name = name;
		this.price = price;
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public Price getPrice() {
		return price;
	}

	public static ProductResponse of(Product product) {
		return new ProductResponse(product.getId(), product.getName(), product.getPrice());
	}
}
