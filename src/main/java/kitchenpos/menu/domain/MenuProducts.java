package kitchenpos.menu.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

@Embeddable
public class MenuProducts {
	@OneToMany(mappedBy = "menu", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
	private List<MenuProduct> menuProducts = new ArrayList<>();

	protected MenuProducts() {
	}

	public MenuProducts(Long menuId, List<MenuProduct> menuProducts) {
		menuProducts.forEach(menuProduct -> menuProduct.setMenuId(menuId));
		this.menuProducts = menuProducts;
	}

	public void addAll(List<MenuProduct> menuProducts) {
		this.menuProducts.addAll(menuProducts);
	}

	public List<MenuProduct> getList() {
		return Collections.unmodifiableList(menuProducts);
	}

	public int sumPrices() {
		return menuProducts.stream()
			.mapToInt(MenuProduct::calculatePrice)
			.sum();
	}
}
