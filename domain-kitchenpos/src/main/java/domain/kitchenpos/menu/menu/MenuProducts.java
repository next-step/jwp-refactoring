package domain.kitchenpos.menu.menu;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

@Embeddable
public class MenuProducts {

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "menu", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<MenuProduct> menuProducts = new ArrayList<>();

	protected MenuProducts() {
	}

	public MenuProducts(final List<MenuProduct> menuProducts) {
		this.menuProducts.addAll(menuProducts);
	}

	public BigDecimal totalPrice() {
		return BigDecimal.valueOf(this.menuProducts.stream()
			.map(MenuProduct::getPrice)
			.mapToLong(BigDecimal::longValue)
			.sum());
	}

	public List<MenuProduct> getMenuProducts() {
		return this.menuProducts;
	}

}
