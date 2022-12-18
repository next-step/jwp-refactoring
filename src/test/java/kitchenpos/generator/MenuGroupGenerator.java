package kitchenpos.generator;

import kitchenpos.menu.domain.MenuGroup;

public class MenuGroupGenerator {

	public static MenuGroup 메뉴_그룹(String name) {
		return new MenuGroup(1L, name);
	}
}
