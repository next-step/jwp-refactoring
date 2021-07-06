package kitchenpos.domain;

import java.math.BigDecimal;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Product {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "name")
	private String name;

	@Embedded
	private Price price = new Price();

	protected Product() {
	}

	public Product(String name, BigDecimal price) {
		this.name = name;
		this.price = new Price(price);
	}

	public Product(Long id, String name, BigDecimal price) {
		this.id = id;
		this.name = name;
		this.price = new Price(price);
	}

	public Long getId() {
		return id;
	}

	public void setId(final Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public BigDecimal getPrice() {
		return price.getPrice();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		Product product = (Product)o;
		return Objects.equals(id, product.id) && Objects.equals(name, product.name) && Objects
			.equals(price, product.price);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, name, price);
	}
}
