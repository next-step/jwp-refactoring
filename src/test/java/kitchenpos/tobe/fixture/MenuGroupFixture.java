package kitchenpos.tobe.fixture;

import kitchenpos.tobe.common.domain.Name;
import kitchenpos.tobe.menu.domain.MenuGroup;

public class MenuGroupFixture {

    private MenuGroupFixture() {
    }

    public static MenuGroup of(final Long id, final Name name) {
        return new MenuGroup(id, name);
    }

    public static MenuGroup of(final Name name) {
        return of(null, name);
    }

    public static MenuGroup of(final String name) {
        return of(new Name(name));
    }
}
