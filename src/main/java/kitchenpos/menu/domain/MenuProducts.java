package kitchenpos.menu.domain;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Stream;

import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

@Embeddable
public class MenuProducts {

	@OneToMany(mappedBy = "menu", fetch = FetchType.EAGER)
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

	public Stream<MenuProduct> stream() {
		return this.menuProducts.stream();
	}

}
