package kitchenpos.product.dto;

import java.math.BigDecimal;

import kitchenpos.common.domain.Name;
import kitchenpos.common.domain.Price;
import kitchenpos.product.domain.Product;

public class ProductCreateRequest {
	private String name;
	private BigDecimal price;

	public ProductCreateRequest() {
	}

	public ProductCreateRequest(String name, BigDecimal price) {
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

	public Product toProduct() {
		return Product.of(Name.of(name), Price.of(price));
	}
}
