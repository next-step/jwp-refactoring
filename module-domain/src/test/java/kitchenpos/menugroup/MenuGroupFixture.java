package kitchenpos.menugroup;

import kitchenpos.common.domain.Name;
import kitchenpos.menugroup.domain.MenuGroup;

public class MenuGroupFixture {
	public static MenuGroup 추천_메뉴_그룹() {
		return MenuGroup.of(1L, Name.from("추천메뉴"));
	}
}
