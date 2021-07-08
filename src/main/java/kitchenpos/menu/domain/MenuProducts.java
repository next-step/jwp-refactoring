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
		BigDecimal sum = BigDecimal.ZERO;
		this.menuProducts.forEach(menuProduct -> {
			sum.add(menuProduct.getProduct().getPrice().multiply(BigDecimal.valueOf(menuProduct.getQuantity())));
		});
		return new Price(sum);
	}

}
