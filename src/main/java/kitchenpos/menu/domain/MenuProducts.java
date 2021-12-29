package kitchenpos.menu.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.Embeddable;

@Embeddable
public class MenuProducts {
	private final List<MenuProduct> menuProducts;

	public MenuProducts() {
		this.menuProducts = new ArrayList<>();
	}

	public MenuProducts(final List<MenuProduct> menuProducts) {
		this.menuProducts = new ArrayList<>(menuProducts);
	}

	public MenuProducts setMenu(Menu menu) {
		return new MenuProducts(menuProducts.stream()
			.map(mp -> new MenuProduct(menu.getId(), mp.getProductId(), mp.getQuantity()))
			.collect(Collectors.toList()));
	}

	public List<MenuProduct> value() {
		return new ArrayList<>(menuProducts);
	}

}
