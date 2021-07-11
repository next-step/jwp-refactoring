package kitchenpos.product.domain;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import kitchenpos.menu.domain.Price;

@Entity
@Table(name = "product")
public class Product {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false, length = 20)
	private Long id;

	@Column(nullable = false)
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

	public String getName() {
		return name;
	}

	public Price getPrice() {
		return price;
	}

}
