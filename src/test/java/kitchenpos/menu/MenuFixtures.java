package kitchenpos.menu;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.Product;

public class MenuFixtures {
	public static Menu aMenu(Long menuGroupId, List<Product> products) {
		return new Menu("menu", 1_000L, menuGroupId, products);
	}

	public static MenuGroup aMenuGroup() {
		return new MenuGroup("menu group");
	}

	public static List<Product> products(int count) {
		return IntStream.range(0, count)
			.mapToObj(i -> new Product("product-"+i, 1_000L))
			.collect(Collectors.toList());
	}
}
