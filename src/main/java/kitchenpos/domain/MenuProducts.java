package kitchenpos.domain;

import kitchenpos.common.Price;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Embeddable
public class MenuProducts implements Iterable<MenuProduct> {

	@Fetch(FetchMode.SELECT)
	@BatchSize(size = 1000)
	@OneToMany(mappedBy = "menu", cascade = {CascadeType.PERSIST}, orphanRemoval = true)
	private List<MenuProduct> menuProducts;

	protected MenuProducts() {
		this(new ArrayList<>());
	}

	public MenuProducts(List<MenuProduct> menuProducts) {
		this.menuProducts = menuProducts;
	}

	@Override
	public Iterator<MenuProduct> iterator() {
		return menuProducts.iterator();
	}

	public Price getAllPrice() {
		return menuProducts.stream()
				.map(MenuProduct::getQuantityPrice)
				.reduce(Price::add)
				.orElse(Price.ZERO);
	}

	public void add(List<MenuProduct> menuProducts) {
		this.menuProducts.addAll(menuProducts);
	}
}
