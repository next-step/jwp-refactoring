package kitchenpos.menu.acceptance.menu;

import java.util.Collections;
import java.util.List;

import org.assertj.core.util.Lists;

import kitchenpos.menu.ui.dto.MenuGroupResponse;
import kitchenpos.menu.ui.dto.MenuRequest;
import kitchenpos.menu.ui.dto.MenuResponse;
import kitchenpos.menu.ui.dto.ProductResponse;

public class MenuFixture {

	public static final long 메뉴가격 = 10_000L;
	private static final String 메뉴명 = "메뉴 1";

	public static MenuRequest 메뉴(ProductResponse product, MenuGroupResponse menuGroup) {
		return 메뉴(Lists.newArrayList(product), menuGroup);
	}

	public static MenuRequest 메뉴(List<ProductResponse> products, MenuGroupResponse menuGroup) {
		Long productsPrice = products.stream()
									 .map(ProductResponse::getPrice)
									 .reduce(0L, Long::sum);

		return 메뉴(products, menuGroup, productsPrice);
	}

	public static MenuRequest 메뉴(List<ProductResponse> products, MenuGroupResponse menuGroup, long price) {
		return new MenuRequest(메뉴명, price, menuGroup.getId(), products);
	}

	public static MenuResponse 메뉴(long id, MenuGroupResponse menuGroup) {
		return new MenuResponse(id, 메뉴명, 메뉴가격, menuGroup.getId(), Collections.emptyList());
	}

	public static MenuRequest 유효하지_않은_가격_메뉴(List<ProductResponse> products, MenuGroupResponse menuGroup) {
		return 메뉴(products, menuGroup, 메뉴가격 - 1);
	}
}
