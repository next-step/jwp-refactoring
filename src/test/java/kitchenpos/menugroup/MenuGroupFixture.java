package kitchenpos.menugroup;

import kitchenpos.domain.MenuGroup;
import kitchenpos.menugroup.dto.MenuGroupCreateRequest;

public class MenuGroupFixture {
	public static MenuGroup 추천_메뉴_그룹() {
		MenuGroup menuGroup = new MenuGroup();
		menuGroup.setId(1L);
		menuGroup.setName("추천메뉴");
		return menuGroup;
	}

	public static MenuGroup 비추천_메뉴_그룹() {
		MenuGroup menuGroup = new MenuGroup();
		menuGroup.setId(2L);
		menuGroup.setName("비추천메뉴");
		return menuGroup;
	}

	public static MenuGroupCreateRequest 추천_메뉴_그룹_요청() {
		return new MenuGroupCreateRequest(추천_메뉴_그룹().getName());
	}

	public static MenuGroupCreateRequest 비추천_메뉴_그룹_요청() {
		return new MenuGroupCreateRequest(비추천_메뉴_그룹().getName());
	}

	public static MenuGroupCreateRequest 이름없는_메뉴_그룹_요청() {
		return new MenuGroupCreateRequest(null);
	}
}
