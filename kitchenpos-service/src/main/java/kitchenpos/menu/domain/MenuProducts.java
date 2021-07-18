package kitchenpos.menu.domain;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Embeddable
public class MenuProducts {
	@OneToMany(mappedBy = "id", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
	private List<MenuProduct> menuProducts = new ArrayList<>();

	public List<MenuProduct> getMenuProducts() {
		return menuProducts;
	}

	public void addMenuProduct(MenuProduct menuProduct) {
		menuProducts.add(menuProduct);
	}

	public void addAllMenuProducts(List<MenuProduct> menuProducts) {
		this.menuProducts.addAll(menuProducts);
	}
}
