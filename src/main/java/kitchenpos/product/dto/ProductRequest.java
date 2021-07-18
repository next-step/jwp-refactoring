package kitchenpos.product.dto;

import kitchenpos.product.domain.Product;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Objects;

public class ProductRequest {
	private String name;
	@NotNull
	@Min(0)
	private BigDecimal price;

	protected ProductRequest() {
	}

	public ProductRequest(String name, BigDecimal price) {
		this.name = name;
		this.price = price;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		ProductRequest that = (ProductRequest) o;
		return Objects.equals(name, that.name) && Objects.equals(price, that.price);
	}

	@Override
	public int hashCode() {
		return Objects.hash(name, price);
	}

	public String getName() {
		return name;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public Product toProduct() {
		return new Product(name, price);
	}
}
