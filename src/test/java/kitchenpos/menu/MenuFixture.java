package kitchenpos.menu;

import java.math.BigDecimal;
import java.util.Collections;

import kitchenpos.menu.dto.MenuCreateRequest;
import kitchenpos.menu.dto.MenuProductDto;

public class MenuFixture {
	public static MenuCreateRequest 후라이드후라이드_메뉴(Long menuGroupId, Long productId) {
		return new MenuCreateRequest(
			"후라이드+후라이드",
			BigDecimal.valueOf(19000L),
			menuGroupId,
			Collections.singletonList(
				new MenuProductDto(null, null, productId, 2)
			)
		);
	}

	public static MenuCreateRequest 양념양념_메뉴(Long menuGroupId, Long productId) {
		return new MenuCreateRequest(
			"양념+양념",
			BigDecimal.valueOf(25000L),
			menuGroupId,
			Collections.singletonList(
				new MenuProductDto(null, null, productId, 2)
			)
		);
	}
}
