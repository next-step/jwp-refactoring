package kitchenpos.product.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import kitchenpos.product.domain.Product;

public class ProductResponse {
	private Long id;
	private String name;
	private Long price;

	public ProductResponse() {
	}

	public ProductResponse(Long id, String name, Long price) {
		this.id = id;
		this.name = name;
		this.price = price;
	}

	public static ProductResponse from(Product product) {
		return new ProductResponse(product.getId(), product.getName(), product.getPrice().longValue());
	}

	public static List<ProductResponse> newList(List<Product> products) {
		return products.stream()
			.map(ProductResponse::from)
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
