package kitchenpos.product.domain;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import kitchenpos.menu.domain.Price;

@Entity
public class Product {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(unique = true)
	private String name;

	@Embedded
	private Price price;

	protected Product() {

	}

	public Product(String name, Price price) {
		this.name = name;
		this.price = price;
	}

	public Product(Long id, String name, Price price) {
		this(name, price);
		this.id = id;
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

	public Price getPrice() {
		return price;
	}

}
