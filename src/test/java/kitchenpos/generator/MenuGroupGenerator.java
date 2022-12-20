package kitchenpos.generator;

import static org.mockito.Mockito.*;

import kitchenpos.menu.domain.MenuGroup;

public class MenuGroupGenerator {

	public static MenuGroup 메뉴_그룹(String name) {
		return MenuGroup.from(name);
	}

	public static MenuGroup 한마리_메뉴() {
		MenuGroup 한마리_메뉴 = spy(MenuGroup.from("한마리 메뉴"));
		lenient().when(한마리_메뉴.getId()).thenReturn(1L);
		return 한마리_메뉴;
	}
}
