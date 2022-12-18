package kitchenpos.menu.ui.response;

import java.util.List;

import kitchenpos.menu.domain.Product;

public class ProductResponse {

	private final Long id;
	private final String name;
	private final Long price;

	private ProductResponse(Long id, String name, Long price) {
		this.id = id;
		this.name = name;
		this.price = price;
	}

	public static ProductResponse of(Long id, String name, Long price) {
		return new ProductResponse(id, name, price);
	}

	public static ProductResponse from(Product save) {
		return new ProductResponse(save.getId(), save.getName(), save.getPrice().longValue());
	}

	public static List<ProductResponse> listFrom(List<Product> products) {
		return products.stream()
			.map(ProductResponse::from)
			.collect(java.util.stream.Collectors.toList());
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
