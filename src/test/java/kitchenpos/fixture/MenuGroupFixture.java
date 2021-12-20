package kitchenpos.fixture;

import kitchenpos.menuGroup.domain.MenuGroup;

public class MenuGroupFixture {

    public static MenuGroup 생성(Long id, String name) {
        return new MenuGroup(id, name);
    }
}
