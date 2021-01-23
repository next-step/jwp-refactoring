package kitchenpos.dto.product;


import kitchenpos.domain.product.Product;

import java.math.BigDecimal;
import java.util.Objects;

public class ProductRequest {
	private String name;
	private BigDecimal price;

	public ProductRequest() {

	}

	public ProductRequest(String name, BigDecimal price) {
		this.name = name;
		this.price = price;
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

	public Product toEntity() {
		validatePrice();
		return new Product(name, price);
	}

	private void validatePrice() {
		if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
			throw new IllegalArgumentException("가격은 0보다 큰 숫자여야 합니다.");
		}
	}
}
