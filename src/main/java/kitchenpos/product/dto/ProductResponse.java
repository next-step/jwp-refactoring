package kitchenpos.product.dto;

import java.util.List;
import java.util.stream.Collectors;

import kitchenpos.domain.Price;
import kitchenpos.product.domain.Product;

public class ProductResponse {

	private Long id;
	private String name;
	private Price price;

	protected ProductResponse() {
	}

	private ProductResponse(Long id, String name, Price price) {
		this.id = id;
		this.name = name;
		this.price = price;
	}

	public static ProductResponse of(Long id, String name, Price price) {
		return new ProductResponse(id, name, price);
	}

	public static ProductResponse of(Long id, String name, int price) {
		return new ProductResponse(id, name, Price.from(price));
	}

	public static List<ProductResponse> ofList(List<Product> list) {
		return list.stream()
			.map(Product::toResDto)
			.collect(Collectors.toList());
	}

	public Long getId() {
		return id;
	}

	public Price getPrice() {
		return price;
	}

	public String getName() {
		return name;
	}
}
