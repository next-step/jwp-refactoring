package kitchenpos.product.dto;

import java.math.BigDecimal;

import kitchenpos.common.domain.Price;
import kitchenpos.domain.Product;
import kitchenpos.product.domain.ProductName;

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
		Product product = new Product();
		product.setName(name);
		product.setPrice(price);
		return product;
	}

	public kitchenpos.product.domain.Product toToBeProduct() {
		return kitchenpos.product.domain.Product.of(ProductName.of(name), Price.of(price));
	}
}
