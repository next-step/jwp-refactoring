package kitchenpos.menugroup;

import kitchenpos.menugroup.dto.MenuGroupCreateRequest;

public class MenuGroupFixture {
	public static MenuGroupCreateRequest 추천_메뉴_그룹() {
		return new MenuGroupCreateRequest("추천메뉴");
	}

	public static MenuGroupCreateRequest 비추천_메뉴_그룹() {
		return new MenuGroupCreateRequest("비추천메뉴");
	}

	public static MenuGroupCreateRequest 이름없는_메뉴_그룹() {
		return new MenuGroupCreateRequest(null);
	}
}
