package kitchenpos.menu;

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
	private Price price;

	protected Product() {
	}

	private Product(String name, Integer price) {
		this.name = name;
		this.price = new Price(price);
	}

	public static ProductBuilder builder() {
		return new ProductBuilder();
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

	public int getPriceValue() {
		return price.value();
	}

	public static final class ProductBuilder {
		private String name;
		private Integer price;

		private ProductBuilder() {
		}

		public ProductBuilder name(String name) {
			this.name = name;
			return this;
		}

		public ProductBuilder price(Integer price) {
			this.price = price;
			return this;
		}

		public Product build() {
			return new Product(name, price);
		}
	}
}
