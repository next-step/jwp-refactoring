package kitchenpos.menugroup.application;

import kitchenpos.domain.MenuGroup;

public class MenuGroupServiceTestHelper {
    public static MenuGroup 메뉴_그룹_생성(Long id, String name) {
        MenuGroup menuGroup = new MenuGroup();
        menuGroup.setId(id);
        menuGroup.setName(name);
        return menuGroup;
    }
}
