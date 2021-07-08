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

	public MenuProducts(List<MenuProduct> menuProducts) {
		this.menuProducts = menuProducts;
	}

	public Price getSumMenuProductPrice() {
		Price sumPrice = new Price(BigDecimal.ZERO);
		this.menuProducts.forEach(menuProduct -> {
			sumPrice.add(menuProduct.getProduct().getPrice().multiply(BigDecimal.valueOf(menuProduct.getQuantity())));
		});
		return sumPrice;
	}

}
