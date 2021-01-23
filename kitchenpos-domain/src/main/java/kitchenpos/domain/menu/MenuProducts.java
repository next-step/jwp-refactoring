package kitchenpos.domain.menu;


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
	private List<MenuProduct> menuProducts = new ArrayList<>();


	protected MenuProducts() {
	}

	public MenuProducts(List<MenuProduct> menuProducts) {
		this.menuProducts = menuProducts;
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