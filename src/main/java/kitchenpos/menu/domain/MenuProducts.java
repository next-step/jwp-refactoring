package kitchenpos.menu.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.product.domain.Price;
import kitchenpos.product.domain.Products;

public class MenuProducts {

	private final List<MenuProduct> menuProducts = new ArrayList<>();

	public MenuProducts(List<MenuProduct> menuProducts) {
		this.menuProducts.addAll(menuProducts);
	}

	public static MenuProducts of(List<MenuProduct> menuProducts) {
		return new MenuProducts(menuProducts);
	}

	public static MenuProducts of(final Products products, final MenuGroup menuGroup, final MenuRequest request) {
		Menu menu = Menu.of(request, menuGroup);

		List<MenuProduct> menuProducts = request.getMenuProducts().stream()
			.map(it -> MenuProduct.of(menu, products.findId(it.getProductId()), it.getQuantity()))
			.collect(Collectors.toList());

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
