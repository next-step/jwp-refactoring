package kitchenpos.fixture;

import kitchenpos.menuGroup.domain.MenuGroup;

public class MenuGroupTestFixture {

    public static MenuGroup 메뉴그룹생성(Long id, String name) {
        return new MenuGroup(id, name);
    }
}
