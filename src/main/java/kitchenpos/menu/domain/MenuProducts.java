package kitchenpos.menu.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

@Embeddable
public class MenuProducts {
	@OneToMany(fetch = FetchType.EAGER, mappedBy = "menu", orphanRemoval = true, cascade = CascadeType.ALL)
	private final List<MenuProduct> menuProducts;

	public MenuProducts() {
		this.menuProducts = new ArrayList<>();
	}

	public MenuProducts(final List<MenuProduct> menuProducts) {
		this.menuProducts = new ArrayList<>(menuProducts);
	}

	public MenuProducts setMenu(Menu menu) {
		return new MenuProducts(menuProducts.stream()
			.map(mp -> new MenuProduct(menu, mp.getProductId(), mp.getQuantity()))
			.collect(Collectors.toList()));
	}

	public List<MenuProduct> value() {
		return new ArrayList<>(menuProducts);
	}
}
