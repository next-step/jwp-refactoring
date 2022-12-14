package kitchenpos.fixture;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import kitchenpos.domain.Menu;
import kitchenpos.domain.Menu2;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Money;
import kitchenpos.domain.Product;

public class MenuFixture {

	private static final String 메뉴명 = "메뉴 1";
	private static final long 메뉴_아이디 = 1L;

	@Deprecated
	public static Menu 메뉴(List<Product> products, MenuGroup menuGroup, int price) {
		Menu menu = new Menu();

		menu.setId(메뉴_아이디);
		menu.setName(메뉴명);
		menu.setPrice(BigDecimal.valueOf(price));
		menu.setMenuProducts(메뉴상품(products));
		menu.setMenuGroupId(menuGroup.getId());

		return menu;
	}

	@Deprecated
	public static Menu 메뉴(List<Product> products, MenuGroup menuGroup) {
		Money price = products.stream()
			.map(Product::getPrice)
			.reduce(Money.ZERO, Money::add);

		return 메뉴(products, menuGroup, price.toBigInteger().intValue());
	}

	@Deprecated
	private static List<MenuProduct> 메뉴상품(List<Product> products) {
		return products.stream()
			.map(product -> {
				MenuProduct menuProduct = new MenuProduct();
				menuProduct.setProductId(product.getId());
				menuProduct.setQuantity(1);
				return menuProduct;
			}).collect(Collectors.toList());
	}

	public static Menu2 메뉴2(List<Product> products, MenuGroup menuGroup) {
		Money productsPrice = products.stream()
			.map(Product::getPrice)
			.reduce(Money.ZERO, Money::add);

		return new Menu2(메뉴명, productsPrice, menuGroup, products);
	}

}
