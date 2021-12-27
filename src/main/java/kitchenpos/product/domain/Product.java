package kitchenpos.product.domain;

import java.math.BigDecimal;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import kitchenpos.common.domain.Name;
import kitchenpos.common.domain.Price;

@Entity
@Table(name = "product")
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

	private Product(Long id, Name name, Price price) {
		this.id = id;
		this.name = name;
		this.price = price;
	}

	public static Product of(Long id, Name name, Price price) {
		return new Product(id, name, price);
	}

	public static Product of(Long id, String name, BigDecimal price) {
		return new Product(id, Name.of(name), Price.of(price));
	}

	public static Product of(String name, BigDecimal price) {
		return new Product(null, Name.of(name), Price.of(price));
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

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		Product product = (Product)o;

		return id.equals(product.id);
	}

	@Override
	public int hashCode() {
		return id.hashCode();
	}
}
