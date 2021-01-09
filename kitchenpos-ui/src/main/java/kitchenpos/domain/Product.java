package kitchenpos.domain;

import java.math.BigDecimal;

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
	private String name;

	@Embedded
	private ProductPrice price;

	protected Product() {
	}

	private Product(String name, BigDecimal price) {
		this.name = name;
		this.price = ProductPrice.of(price);
	}

	public static Product create(String name, BigDecimal price) {
		return new Product(name, price);
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
}
