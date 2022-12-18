package kitchenpos.acceptance.menu;

import java.util.Collections;
import java.util.List;

import kitchenpos.ui.dto.MenuGroupResponse;
import kitchenpos.ui.dto.MenuRequest;
import kitchenpos.ui.dto.MenuResponse;
import kitchenpos.ui.dto.ProductResponse;

public class MenuFixture {

	private static final String 메뉴명 = "메뉴 1";
	public static final long 메뉴가격 = 10_000L;

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
