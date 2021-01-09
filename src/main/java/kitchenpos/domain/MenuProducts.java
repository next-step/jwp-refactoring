package kitchenpos.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

@Embeddable
public class MenuProducts {

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "menu_id")
	private List<MenuProduct> menuProducts = new ArrayList<>();

	public void add(MenuProduct menuProduct) {
		this.menuProducts.add(menuProduct);
	}

	public List<MenuProduct> getMenuProducts() {
		return menuProducts;
	}

}
