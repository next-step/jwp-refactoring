package kitchenpos.menu.domain;


import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Embeddable
public class MenuProducts {

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "menu_id")
	private final List<MenuProduct> menuProducts = new ArrayList<>();


	protected MenuProducts() {
	}

	public List<MenuProduct> getMenuProducts() {
		return menuProducts;
	}

	public void addAll(List<MenuProduct> menuProducts) {
		this.menuProducts.addAll(menuProducts);
	}


	public void add(MenuProduct menuProduct) {
		this.menuProducts.add(menuProduct);
	}

}