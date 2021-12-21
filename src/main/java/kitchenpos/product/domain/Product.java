package kitchenpos.product.domain;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import kitchenpos.common.domain.Name;
import kitchenpos.common.domain.Price;

@Table(name = "product")
@Entity
public class Product {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Embedded
	private Name name;

	@Embedded
	private Price price;

	protected Product() {
	}

	public static Product of(Name name, Price price) {
		Product product = new Product();
		product.name = name;
		product.price = price;
		return product;
	}

	public Long getId() {
		return id;
	}

	public Name getName() {
		return name;
	}

	public Price getPrice() {
		return price;
	}
}
