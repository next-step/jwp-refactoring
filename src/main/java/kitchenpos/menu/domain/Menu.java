package kitchenpos.menu.domain;

import kitchenpos.common.BaseIdEntity;
import kitchenpos.common.Price;
import kitchenpos.menu.application.MenuValidationException;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "menu")
public class Menu extends BaseIdEntity {

	static final String MSG_PRICE_RULE = "Menu price must be equal or low than products prices' total sum";

	@Column(name = "name", nullable = false)
	private String name;

	@Embedded
	private Price price;

	@ManyToOne
	@JoinColumn(name = "menu_group_id", nullable = false)
	private MenuGroup menuGroup;

	@Embedded
	private MenuProducts menuProducts;

	protected Menu() {
	}

	public Menu(String name, int price, MenuGroup menuGroup) {
		this(name, new Price(new BigDecimal(price)), menuGroup);
	}

	public Menu(String name, Price price, MenuGroup menuGroup) {
		this.name = name;
		this.price = price;
		this.menuGroup = menuGroup;
		this.menuProducts = new MenuProducts(new ArrayList<>());
	}

	public void addMenuProducts(List<MenuProduct> menuProducts) {
		validatePrice(menuProducts);
		this.menuProducts.add(menuProducts);
	}

	private void validatePrice(List<MenuProduct> menuProducts) {
		Price beforePriceSum = this.menuProducts.getAllPrice();
		Price afterPriceSum = beforePriceSum.add(new MenuProducts(menuProducts).getAllPrice());
		if (this.price.compareTo(afterPriceSum) > 0) {
			throw new MenuValidationException(MSG_PRICE_RULE);
		}
	}

	public String getName() {
		return name;
	}

	public Price getPrice() {
		return price;
	}

	public MenuGroup getMenuGroup() {
		return menuGroup;
	}

	public Iterable<MenuProduct> getMenuProducts() {
		return menuProducts;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Menu)) return false;
		if (!super.equals(o)) return false;
		Menu menu = (Menu) o;
		return Objects.equals(name, menu.name) &&
				Objects.equals(price, menu.price) &&
				Objects.equals(menuGroup, menu.menuGroup) &&
				Objects.equals(menuProducts, menu.menuProducts);
	}

	@Override
	public int hashCode() {
		return Objects.hash(super.hashCode(), name, price, menuGroup, menuProducts);
	}
}
