package kitchenpos.domain;

import kitchenpos.menu.domain.MenuGroup;

public class MenuGroupTest {

    public static MenuGroup 메뉴_그룹_생성(String name) {
        return new MenuGroup(name);
    }
}
