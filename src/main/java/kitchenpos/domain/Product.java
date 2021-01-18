package kitchenpos.domain;

import kitchenpos.common.BaseIdEntity;
import kitchenpos.common.Price;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "product")
public class Product extends BaseIdEntity {

	@Column(name = "name", nullable = false)
	private String name;

	@Embedded
	private Price price;

	protected Product() {
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
}
