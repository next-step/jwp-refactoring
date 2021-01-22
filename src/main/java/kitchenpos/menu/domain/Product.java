package kitchenpos.menu.domain;

import kitchenpos.common.entity.BaseIdEntity;
import kitchenpos.common.entity.Price;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "product")
public class Product extends BaseIdEntity {

	@Column(name = "name", nullable = false)
	private String name;

	@Embedded
	private Price price;

	protected Product() {
	}

	public Product(String name, int price) {
		this(name, new Price(new BigDecimal(price)));
	}

	public Product(String name, Price price) {
		this.name = name;
		this.price = price;
	}

	public String getName() {
		return name;
	}

	public Price getPrice() {
		return price;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Product)) return false;
		if (!super.equals(o)) return false;
		Product product = (Product) o;
		return Objects.equals(name, product.name) &&
				Objects.equals(price, product.price);
	}

	@Override
	public int hashCode() {
		return Objects.hash(super.hashCode(), name, price);
	}
}
