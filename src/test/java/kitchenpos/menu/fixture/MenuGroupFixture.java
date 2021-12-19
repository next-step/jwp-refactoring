package kitchenpos.menu.fixture;

import kitchenpos.common.domain.fixture.NameFixture;
import kitchenpos.menu.domain.MenuGroup;

public class MenuGroupFixture {
    private MenuGroupFixture() {
        throw new UnsupportedOperationException();
    }

    public static MenuGroup create(Long id, String name) {
        return MenuGroup.of(id, NameFixture.of(name));
    }
}
