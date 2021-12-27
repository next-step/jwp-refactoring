package kitchenpos.menu.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

import kitchenpos.common.domain.Price;

@Embeddable
public class MenuProducts {

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "menu")
	private List<MenuProduct> menuProducts = new ArrayList<>();

	protected MenuProducts() {
	}

	private MenuProducts(List<MenuProduct> menuProducts) {
		this.menuProducts.addAll(menuProducts);
	}

	public static MenuProducts of(List<MenuProduct> menuProducts) {
		return new MenuProducts(menuProducts);
	}

	public static MenuProducts empty() {
		return new MenuProducts(new ArrayList<>());
	}

	public Price getTotalPrice() {
		return menuProducts.stream().map(MenuProduct::getTotalPrice)
			.reduce(Price.ZERO, Price::add);
	}

	public void addAll(MenuProducts others) {
		this.menuProducts.addAll(others.menuProducts);
	}

	public List<MenuProduct> toList() {
		return Collections.unmodifiableList(menuProducts);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		MenuProducts that = (MenuProducts)o;

		return menuProducts.equals(that.menuProducts);
	}

	@Override
	public int hashCode() {
		return menuProducts.hashCode();
	}
}
