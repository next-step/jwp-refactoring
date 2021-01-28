package kitchenpos.menu.domain;

import java.util.ArrayList;
import java.util.List;

import kitchenpos.product.domain.Price;

public class MenuProducts {

	private final List<MenuProduct> menuProducts = new ArrayList<>();

	public MenuProducts(List<MenuProduct> menuProducts) {
		this.menuProducts.addAll(menuProducts);
	}

	private static MenuProducts of(List<MenuProduct> menuProducts) {
		return new MenuProducts(menuProducts);
	}

	public static MenuProducts of(Menu menu, List<MenuProduct> menuProducts) {
		validateMenuProducts(menuProducts, menu.getPrice());
		return of(menuProducts);
	}

	private static void validateMenuProducts(List<MenuProduct> menuProducts, Price menuPrice) {
		Price totalPrice = Price.of(menuProducts.stream()
			.mapToLong(MenuProduct::menuProductPrice)
			.sum());

		if (menuPrice.isBigger(totalPrice)) {
			throw new IllegalArgumentException("메뉴의 가격이 상품가격 합보다 더 큽니다.");
		}
	}

	public List<MenuProduct> getMenuProducts() {
		return this.menuProducts;
	}
}
