package kitchenpos.menu.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

@Embeddable
public class MenuProducts {

	@OneToMany(mappedBy = "menu")
	private List<MenuProduct> menuProducts = new ArrayList<>();

	public void addMenuProduct(MenuProduct menuProduct) {
		this.menuProducts.add(menuProduct);
	}

	public Price getTotalPrice() {
		Price totalPrice = new Price(BigDecimal.ZERO);
		for (MenuProduct menuProduct : menuProducts) {
			totalPrice.addPrice(menuProduct.getTotalPrice());
		}
		return totalPrice;
	}

	public List<MenuProduct> getMenuProducts() {
		return menuProducts;
	}
}
