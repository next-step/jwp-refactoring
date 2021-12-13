package kitchenpos.application.fixture;

import kitchenpos.domain.MenuGroup;

public class MenuGroupFixture {
    private MenuGroupFixture() {
        throw new UnsupportedOperationException();
    }

    public static MenuGroup create(Long id, String name) {
        MenuGroup menuGroup = new MenuGroup();
        menuGroup.setId(id);
        menuGroup.setName(name);

        return menuGroup;
    }
}
