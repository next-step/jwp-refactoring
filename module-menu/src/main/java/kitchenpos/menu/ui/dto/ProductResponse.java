package kitchenpos.menu.ui.dto;

import java.util.List;
import java.util.stream.Collectors;

import kitchenpos.menu.domain.Product;

public class ProductResponse {

	private Long id;
	private String name;
	private Long price;

	private ProductResponse() {
	}

	public ProductResponse(Long id, String name, Long price) {
		this.id = id;
		this.name = name;
		this.price = price;
	}

	public ProductResponse(Product product) {
		this(product.getId(),
			product.getName()
				.value(),
			product.getPrice()
				.longValue());
	}

	public static List<ProductResponse> of(List<Product> products) {
		return products.stream()
			.map(ProductResponse::new)
			.collect(Collectors.toList());
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public Long getPrice() {
		return price;
	}
}
