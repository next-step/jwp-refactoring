package kitchenpos.menu.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

import kitchenpos.common.domain.Price;

@Embeddable
public class MenuProducts {
	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "menu_id", nullable = false)
	private List<MenuProduct> values = new ArrayList<>();

	protected MenuProducts() {
	}

	public static MenuProducts from(List<MenuProduct> values) {
		if (values == null || values.isEmpty()) {
			throw new IllegalArgumentException("메뉴 상품들은 한개 이상이어야 합니다.");
		}

		MenuProducts menuProducts = new MenuProducts();
		menuProducts.values = values;
		return menuProducts;
	}

	public List<MenuProduct> getValues() {
		return values;
	}

	public Price getTotalPrice() {
		return values.stream()
			.map(MenuProduct::getTotalPrice)
			.reduce(Price::add)
			.get();
	}
}
