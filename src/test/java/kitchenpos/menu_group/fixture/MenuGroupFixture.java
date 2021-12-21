package kitchenpos.menu_group.fixture;

import kitchenpos.common.domain.fixture.NameFixture;
import kitchenpos.menu_group.domain.MenuGroup;

public class MenuGroupFixture {
    private MenuGroupFixture() {
        throw new UnsupportedOperationException();
    }

    public static MenuGroup create(Long id, String name) {
        return MenuGroup.of(id, NameFixture.of(name));
    }
}
