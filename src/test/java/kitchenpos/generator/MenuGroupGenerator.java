package kitchenpos.generator;

import kitchenpos.menu.domain.MenuGroup;

public class MenuGroupGenerator {

	public static MenuGroup 메뉴_그룹(String name) {
		return MenuGroup.from(name);
	}

	public static MenuGroup 한마리_메뉴() {
		return 메뉴_그룹("한마리 메뉴");
	}
}
