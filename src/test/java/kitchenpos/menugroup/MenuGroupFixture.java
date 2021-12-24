package kitchenpos.menugroup;

import kitchenpos.common.domain.Name;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menugroup.dto.MenuGroupCreateRequest;

public class MenuGroupFixture {
	public static MenuGroup 추천_메뉴_그룹() {
		return MenuGroup.of(1L, Name.from("추천메뉴"));
	}

	public static MenuGroupCreateRequest 추천_메뉴_그룹_요청() {
		return new MenuGroupCreateRequest("추천메뉴");
	}

	public static MenuGroupCreateRequest 비추천_메뉴_그룹_요청() {
		return new MenuGroupCreateRequest("비추천메뉴");
	}

	public static MenuGroupCreateRequest 이름없는_메뉴_그룹_요청() {
		return new MenuGroupCreateRequest(null);
	}
}
