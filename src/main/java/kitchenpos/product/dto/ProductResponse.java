package kitchenpos.product.dto;

import kitchenpos.product.domain.Product;

public class ProductResponse {
	private Long id;
	private String name;
	private long price;

	public ProductResponse() {
	}

	private ProductResponse(Long id, String name, Long price) {
		this.id = id;
		this.name = name;
		this.price = price;
	}

	public static ProductResponse of(Long id, String name, Long price) {
		return new ProductResponse(id, name, price);
	}

	public static ProductResponse of(Product product) {
		return of(product.getId(), product.getName(), product.price());
	}

	public Long getId() {
		return this.id;
	}

	public String getName() {
		return name;
	}

	public long getPrice() {
		return price;
	}
}
