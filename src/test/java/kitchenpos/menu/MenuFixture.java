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

	public static MenuCreateRequest 이름없는_메뉴(Long menuGroupId, Long productId) {
		return new MenuCreateRequest(
			null,
			BigDecimal.valueOf(19000L),
			menuGroupId,
			Collections.singletonList(
				new MenuProductDto(null, null, productId, 2)
			)
		);
	}

	public static MenuCreateRequest 음수가격_메뉴(Long menuGroupId, Long productId) {
		return new MenuCreateRequest(
			"사시면+돈도드려요",
			BigDecimal.valueOf(-19000L),
			menuGroupId,
			Collections.singletonList(
				new MenuProductDto(null, null, productId, 2)
			)
		);
	}

	public static MenuCreateRequest 너무비싼_메뉴(Long menuGroupId, Long productId) {
		return new MenuCreateRequest(
			"사면+호구",
			BigDecimal.valueOf(100000L),
			menuGroupId,
			Collections.singletonList(
				new MenuProductDto(null, null, productId, 2)
			)
		);
	}
}
