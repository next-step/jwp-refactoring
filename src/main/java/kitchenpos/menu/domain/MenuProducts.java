package kitchenpos.menu.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

import kitchenpos.common.ErrorCode;
import kitchenpos.common.PriceException;
import kitchenpos.domain.Price;

@Embeddable
public class MenuProducts {

	public static final MenuProducts EMPTY_MENU_PRODUCTS = new MenuProducts();

	@OneToMany(mappedBy = "menu", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	private List<MenuProduct> menuProducts = new ArrayList<>();

	protected MenuProducts() {
	}

	private MenuProducts(final List<MenuProduct> menuProducts) {
		this.menuProducts = menuProducts;
	}

	public static MenuProducts from(final List<MenuProduct> menuProducts) {
		return new MenuProducts(menuProducts);
	}

	public List<MenuProduct> getMenuProducts() {
		return menuProducts;
	}

	public void addList(List<MenuProduct> savedMenuProducts) {
		menuProducts = savedMenuProducts;
	}

	public void addList(List<MenuProduct> savedMenuProducts, Price menuPrice) {
		menuProducts = savedMenuProducts;
		validateMenuPrice(menuPrice);
	}

	public Price getSum() {
		return menuProducts.stream()
			.map(MenuProduct::getTotalPrice)
			.reduce(Price.ZERO_PRICE, Price::plus);
	}

	public boolean comparePrice(Price menuPrice) {
		return menuPrice.compare(getSum());
	}

	public void validateMenuPrice(Price menuPrice) {
		if (comparePrice(menuPrice)) {
			throw new PriceException(ErrorCode.PRODUCT_PRICE_IS_UNDER_SUM_PRICE);
		}
	}
}
