package kitchenpos.menu.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

import kitchenpos.common.Price;

@Embeddable
public class MenuProducts {
	@OneToMany(fetch = FetchType.EAGER, mappedBy = "menu", orphanRemoval = true, cascade = CascadeType.ALL)
	private final List<MenuProduct> menuProducts;

	public MenuProducts() {
		this.menuProducts = new ArrayList<>();
	}

	public MenuProducts(final List<MenuProduct> menuProducts) {
		this.menuProducts = new ArrayList<>(menuProducts);
	}

	public Price sumProductPrice() {
		return Price.valueOf(menuProducts.stream()
			.map(mp -> mp.getProductPrice().multiply(BigDecimal.valueOf(mp.getQuantity())))
			.reduce(BigDecimal::add)
			.orElseThrow(IllegalArgumentException::new));
	}

	public MenuProducts setMenu(Menu menu) {
		return new MenuProducts(menuProducts.stream()
			.map(mp -> new MenuProduct(menu, mp.getProduct(), mp.getQuantity()))
			.collect(Collectors.toList()));
	}

	public List<MenuProduct> value() {
		return new ArrayList<>(menuProducts);
	}
}
