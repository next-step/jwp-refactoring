package kitchenpos.product.dto;

import java.math.BigDecimal;

import kitchenpos.product.domain.Product;

public class ProductResponse {

	private Long id;
	private String name;
	private BigDecimal price;

	public ProductResponse() {
	}

	public ProductResponse(Long id, String name, BigDecimal price) {
		this.id = id;
		this.name = name;
		this.price = price;
	}

	public ProductResponse(Product product) {
		this.id = product.getId();
		this.name = product.getName().toText();
		this.price = product.getPrice().toBigDecimal();
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
