package kitchenpos.menu;

import kitchenpos.menu.dto.MenuGroupCreateRequest;

public class MenuGroupFixture {
	public static MenuGroupCreateRequest 추천메뉴() {
		return new MenuGroupCreateRequest("추천메뉴");
	}

	public static MenuGroupCreateRequest 비추천메뉴() {
		return new MenuGroupCreateRequest("비추천메뉴");
	}

	public static MenuGroupCreateRequest 이름없는메뉴그룹() {
		return new MenuGroupCreateRequest(null);
	}
}
