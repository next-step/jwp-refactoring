package kitchenpos.menu.application.sample;

import kitchenpos.menu.domain.MenuGroup;

public class MenuGroupSample {

    public static MenuGroup 두마리메뉴() {
        MenuGroup menuGroup = new MenuGroup();
        menuGroup.setId(1L);
        menuGroup.setName("두마리메뉴");
        return menuGroup;
    }
}
