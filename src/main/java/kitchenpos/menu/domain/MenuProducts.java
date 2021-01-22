package kitchenpos.menu.domain;

import kitchenpos.common.entity.Price;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

@Embeddable
public class MenuProducts implements Iterable<MenuProduct> {

	@Fetch(FetchMode.SELECT)
	@BatchSize(size = 1000)
	@OneToMany(mappedBy = "menu", cascade = {CascadeType.PERSIST}, orphanRemoval = true)
	private List<MenuProduct> menuProducts;

	MenuProducts() {
		this(new ArrayList<>());
	}

	MenuProducts(List<MenuProduct> menuProducts) {
		this.menuProducts = menuProducts;
	}

	@Override
	public Iterator<MenuProduct> iterator() {
		return menuProducts.iterator();
	}

	Price getAllPrice() {
		return menuProducts.stream()
				.map(MenuProduct::getQuantityPrice)
				.reduce(Price::add)
				.orElse(Price.ZERO);
	}

	void add(List<MenuProduct> menuProducts) {
		this.menuProducts.addAll(menuProducts);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof MenuProducts)) return false;
		MenuProducts that = (MenuProducts) o;
		return Objects.equals(menuProducts, that.menuProducts);
	}

	@Override
	public int hashCode() {
		return Objects.hash(menuProducts);
	}
}
