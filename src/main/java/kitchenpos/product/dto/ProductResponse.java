package kitchenpos.product.dto;

import kitchenpos.product.domain.Product;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

public class ProductResponse {
	private long id;
	private String name;
	private BigDecimal price;

	public ProductResponse() {
	}

	public ProductResponse(long id, String name, BigDecimal price) {
		this.id = id;
		this.name = name;
		this.price = price;
	}

	public static List<ProductResponse> of(List<Product> products) {
		return products.stream()
				.map(ProductResponse::of)
				.collect(Collectors.toList());
	}

	public static ProductResponse of(Product product) {
		return new ProductResponse(product.getId(), product.getName(), product.getPrice());
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}
}
