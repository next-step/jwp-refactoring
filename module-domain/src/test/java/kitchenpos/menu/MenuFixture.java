package kitchenpos.menu;

import static kitchenpos.menu.MenuProductFixture.*;
import static kitchenpos.menugroup.MenuGroupFixture.*;

import java.math.BigDecimal;
import java.util.Collections;

import kitchenpos.common.domain.Name;
import kitchenpos.common.domain.Price;
import kitchenpos.menu.domain.ValidMenuValidator;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.dto.MenuCreateRequest;
import kitchenpos.menu.dto.MenuProductDto;

public class MenuFixture {
	public static Menu 후라이드후라이드_메뉴() {
		return Menu.of(
			1L,
			Name.from("후라이드+후라이드"),
			Price.from(BigDecimal.valueOf(19000)),
			추천_메뉴_그룹().getId(),
			Collections.singletonList(MenuProductDto.from(후라이드치킨_2개_메뉴_상품())),
			new ValidMenuValidator());
	}

	public static MenuCreateRequest 후라이드후라이드_메뉴_요청(Long menuGroupId, Long productId) {
		return new MenuCreateRequest(
			"후라이드+후라이드",
			BigDecimal.valueOf(19000L),
			menuGroupId,
			Collections.singletonList(
				new MenuProductDto(null, productId, 2)
			)
		);
	}

	public static MenuCreateRequest 양념양념_메뉴_요청(Long menuGroupId, Long productId) {
		return new MenuCreateRequest(
			"양념+양념",
			BigDecimal.valueOf(25000L),
			menuGroupId,
			Collections.singletonList(
				new MenuProductDto(null, productId, 2)
			)
		);
	}

	public static MenuCreateRequest 이름없는_메뉴_요청(Long menuGroupId, Long productId) {
		return new MenuCreateRequest(
			null,
			BigDecimal.valueOf(19000L),
			menuGroupId,
			Collections.singletonList(
				new MenuProductDto(null, productId, 2)
			)
		);
	}

	public static MenuCreateRequest 음수가격_메뉴_요청(Long menuGroupId, Long productId) {
		return new MenuCreateRequest(
			"사시면+돈도드려요",
			BigDecimal.valueOf(-19000L),
			menuGroupId,
			Collections.singletonList(
				new MenuProductDto(null, productId, 2)
			)
		);
	}

	public static MenuCreateRequest 너무비싼_메뉴_요청(Long menuGroupId, Long productId) {
		return new MenuCreateRequest(
			"사면+호구",
			BigDecimal.valueOf(100000L),
			menuGroupId,
			Collections.singletonList(
				new MenuProductDto(null, productId, 2)
			)
		);
	}
}
