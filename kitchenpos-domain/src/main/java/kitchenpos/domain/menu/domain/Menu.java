package kitchenpos.domain.menu.domain;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Menu {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String name;
	private BigDecimal price;
	private Long menuGroupId;

	@Embedded
	private MenuProducts menuProducts = new MenuProducts();

	public Menu(String name, BigDecimal price, long menuGroupId, MenuProducts menuProducts) {
		validatePrice(price);
		this.name = name;
		this.price = price;
		this.menuGroupId = menuGroupId;
		this.menuProducts = menuProducts;
	}

	public Menu(String name, BigDecimal price, Long menuGroupId) {
		this(name, price, menuGroupId, null);
	}

	private void validatePrice(BigDecimal price) {
		if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
			throw new IllegalArgumentException();
		}
	}

	public void validateSumOfPriceToAddMenuProduct(List<MenuProduct> menuProducts) {
		BigDecimal menuProductSumOfPrice = menuProducts.stream()
			.map(menuProduct -> menuProduct.getProduct()
				.getPrice()
				.multiply(BigDecimal.valueOf(menuProduct.getQuantity())))
			.reduce(BigDecimal.ZERO, BigDecimal::add);

		if (price.compareTo(menuProductSumOfPrice) > 0) {
			throw new IllegalArgumentException();
		}
	}

	public void addMenuProducts(List<MenuProduct> menuProducts) {
		if (this.menuProducts == null) {
			this.menuProducts = new MenuProducts();
		}
		validateSumOfPriceToAddMenuProduct(menuProducts);
		menuProducts.forEach(menuProduct -> this.menuProducts.add(menuProduct));
	}


	public static class Builder {
		private String name;
		private BigDecimal price;
		private Long menuGroupId;
		private MenuProducts menuProducts;

		public Builder() {
		}

		public Builder name(String name) {
			this.name = name;
			return this;
		}

		public Builder price(BigDecimal price) {
			this.price = price;
			return this;
		}

		public Builder menuGroupId(Long menuGroupId) {
			this.menuGroupId = menuGroupId;
			return this;
		}

		public Builder menuProducts(MenuProducts menuProducts) {
			this.menuProducts = menuProducts;
			return this;
		}

		public Menu build() {
			return new Menu(name, price, menuGroupId, menuProducts);
		}

	}

	public Menu() {
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public Long getMenuGroupId() {
		return menuGroupId;
	}

	public List<MenuProduct> getMenuProducts() {
		return menuProducts.getMenuProducts();
	}

}
