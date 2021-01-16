package kitchenpos.product.dto;

import java.math.BigDecimal;
import kitchenpos.product.domain.Product;

public class ProductRequest {
	private String name;
	private BigDecimal price;

	public ProductRequest(String name, BigDecimal price) {
		this.name = name;
		this.price = price;
	}

	public Product toEntity() {
		return new Product(this.name, this.price);
	}

	public String getName() {
		return name;
	}

	public BigDecimal getPrice() {
		return price;
	}
}
