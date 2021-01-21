package kitchenpos.product.domain;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import kitchenpos.common.BaseEntity;

@Entity
public class Product extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String name;
	@Embedded
	private Price price;

	protected Product() {
	}

	protected Product(String name, long price) {
		this.name = name;
		this.price = Price.of(price);
	}

	public static Product of(String name, long price) {
		return new Product(name, price);
	}

	public Long getId() {
		return this.id;
	}

	public String getName() {
		return this.name;
	}

	public Long price() {
		return this.price.getPrice();
	}
}
