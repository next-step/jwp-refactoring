package kitchenpos.menu;

import kitchenpos.menu.domain.MenuGroup;

public class MenuGroupTestFixture {
    public static MenuGroup 메뉴_그룹_생성(Long id, String name) {
        return new MenuGroup(id, name);
    }
}
