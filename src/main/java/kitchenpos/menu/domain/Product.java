package kitchenpos.menu.domain;

import java.util.Objects;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "product")
public class Product {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Embedded
	private Name name;

	@Embedded
	@AttributeOverride(name = "value", column = @Column(name = "price"))
	private Money price;

	protected Product() {
	}

	public Product(Long id, String name, Money price) {
		this.id = id;
		this.name = new Name(name);
		this.price = price;
	}

	public Product(Long id, String name, int price) {
		this(id, name, Money.valueOf(price));
	}

	public Product(String name, Money price) {
		this(null, name, price);
	}

	public Product(String name, Long price) {
		this(name, Money.valueOf(price));
	}

	public Product(Long id) {
		this(id, null, null);
	}

	public Long getId() {
		return id;
	}

	public Name getName() {
		return name;
	}

	public Money getPrice() {
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
		return Objects.hash(id);
	}
}
