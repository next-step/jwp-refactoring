package kitchenpos.menu.domain;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

@Embeddable
public class MenuProducts {

	@OneToMany(fetch = FetchType.EAGER)
	private List<MenuProduct> menuProducts;

	protected MenuProducts() {

	}

	public MenuProducts(List<MenuProduct> menuProducts) {
		this.menuProducts = menuProducts;
	}

	public Price getSumMenuProductPrice() {
		BigDecimal sum = this.menuProducts.stream()
			.map(menuProduct -> menuProduct.getMenuProductPrice().value())
			.reduce(BigDecimal.ZERO, BigDecimal::add);
		return new Price(sum);
	}

}
