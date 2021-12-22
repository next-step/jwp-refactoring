package kitchenpos.menu.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

@Embeddable
public class MenuProducts {

	public static final MenuProducts EMPTY_MENU_PRODUCTS = new MenuProducts();

	@OneToMany(mappedBy = "menu", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	private List<MenuProduct> menuProducts = new ArrayList<>();

	protected MenuProducts() {
	}

	private MenuProducts(final List<MenuProduct> menuProducts) {
		this.menuProducts = menuProducts;
	}

	public static MenuProducts from(final List<MenuProduct> menuProducts) {
		return new MenuProducts(menuProducts);
	}

	public List<MenuProduct> getMenuProducts() {
		return menuProducts;
	}

	public void addList(List<MenuProduct> savedMenuProducts) {
		menuProducts = savedMenuProducts;
	}
}
