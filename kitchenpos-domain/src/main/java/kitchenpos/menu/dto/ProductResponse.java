package kitchenpos.menu.dto;

import java.util.List;
import java.util.stream.Collectors;

import kitchenpos.menu.Product;

public class ProductResponse {
	private Long id;
	private String name;
	private long price;

	protected ProductResponse() {
	}

	public ProductResponse(Long id, String name, long price) {
		this.id = id;
		this.name = name;
		this.price = price;
	}

	public static ProductResponse of(Product product) {
		return new ProductResponse(product.getId(), product.getName(), product.getPriceValue());
	}

	public static List<ProductResponse> of(List<Product> products) {
		return products.stream()
			.map(ProductResponse::of)
			.collect(Collectors.toList());
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public long getPrice() {
		return price;
	}
}
