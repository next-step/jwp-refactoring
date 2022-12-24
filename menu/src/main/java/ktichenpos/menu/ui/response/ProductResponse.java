package ktichenpos.menu.ui.response;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import kitchenpos.menu.domain.Product;

public class ProductResponse {

	private Long id;
	private String name;
	private BigDecimal price;

	public ProductResponse() {
	}

	private ProductResponse(Long id, String name, BigDecimal price) {
		this.id = id;
		this.name = name;
		this.price = price;
	}

	public static ProductResponse of(Long id, String name, BigDecimal price) {
		return new ProductResponse(id, name, price);
	}

	public static ProductResponse from(Product product) {
		return new ProductResponse(product.id(), product.name(), product.price().value());
	}

	public static List<ProductResponse> listFrom(List<Product> products) {
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

	public BigDecimal getPrice() {
		return price;
	}

}
