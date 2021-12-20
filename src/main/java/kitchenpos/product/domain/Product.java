package kitchenpos.product.domain;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import kitchenpos.common.Price;

@Entity
public class Product {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Embedded
	private ProductName name;

	@Embedded
	private Price price;

	protected Product() {
	}

	public static Product of(ProductName name, Price price) {
		Product product = new Product();
		product.name = name;
		product.price = price;
		return product;
	}

	public Long getId() {
		return id;
	}

	public ProductName getName() {
		return name;
	}

	public Price getPrice() {
		return price;
	}
}
