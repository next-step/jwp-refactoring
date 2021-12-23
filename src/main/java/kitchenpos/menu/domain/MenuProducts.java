package kitchenpos.menu.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

@Embeddable
public class MenuProducts {

	@OneToMany(mappedBy = "menu", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<MenuProduct> menuProducts = new ArrayList<>();

	protected MenuProducts() {
	}

	private MenuProducts(List<MenuProduct> menuProducts) {
		this.menuProducts = menuProducts;
	}

	public static MenuProducts of() {
		return of(new ArrayList<>());
	}

	public static MenuProducts of(List<MenuProduct> menuProducts) {
		return new MenuProducts(menuProducts);
	}

	public void addAll(Menu menu, List<MenuProduct> menuProducts) {
		this.menuProducts.addAll(menuProducts);
		this.menuProducts.forEach(menuProduct -> menuProduct.setMenu(menu));
	}

	public List<MenuProduct> getMenuProducts() {
		return Collections.unmodifiableList(menuProducts);
	}
}
