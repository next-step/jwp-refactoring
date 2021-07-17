package kitchenpos.menu.domain;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

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

	public Menu() {
	}

	public Menu(String name, BigDecimal price, Long menuGroupId) {
		this.name = name;
		this.price = price;
		this.menuGroupId = menuGroupId;
	}

	public Menu(Long id, String name, BigDecimal price, Long menuGroupId) {
		this.id = id;
		this.name = name;
		this.price = price;
		this.menuGroupId = menuGroupId;
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

	public void addMenuProduct(final List<MenuProduct> menuProducts) {
		validatePrice(menuProducts);
		this.menuProducts.addAllMenuProducts(menuProducts);
	}

	private void validatePrice(List<MenuProduct> menuProducts) {
		BigDecimal sumOfProductsPrice = menuProducts.stream().map(menuProduct -> menuProduct.getProduct().getPrice()
				.multiply(BigDecimal.valueOf(menuProduct.getQuantity())))
				.reduce(BigDecimal.ZERO, BigDecimal::add);

		if (price.compareTo(sumOfProductsPrice) > 0) {
			throw new IllegalArgumentException("메뉴의 가격이 부정확합니다.");
		}
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Menu menu = (Menu) o;
		return Objects.equals(id, menu.id) && Objects.equals(name, menu.name) && Objects.equals(price, menu.price) && Objects.equals(menuGroupId, menu.menuGroupId);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, name, price, menuGroupId, menuProducts);
	}
}
