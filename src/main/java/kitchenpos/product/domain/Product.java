package kitchenpos.product.domain;

import java.math.BigDecimal;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import kitchenpos.domain.Price;
import kitchenpos.product.dto.ProductRequest;
import kitchenpos.product.dto.ProductResponse;

@Entity
public class Product {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String name;

	@Embedded
	private Price price;

	protected Product() {
	}

	private Product(String name, Price price) {
		this.name = name;
		this.price = price;
	}

	public static Product of(String name, int price) {
		return new Product(name, Price.from(price));
	}

	public static Product from(ProductRequest productRequest) {
		return new Product(productRequest.getName(), Price.from(productRequest.getPrice()));
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public BigDecimal getPrice() {
		return price.getPrice();
	}

	public ProductResponse toResDto() {
		return ProductResponse.of(id, name, price);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		Product product = (Product)o;
		return Objects.equals(id, product.id);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}
}
