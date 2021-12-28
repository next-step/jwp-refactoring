package kitchenpos.tobe.fixture;

import kitchenpos.tobe.common.domain.Name;
import kitchenpos.tobe.menus.menugroup.domain.MenuGroup;
import kitchenpos.tobe.menus.menugroup.dto.MenuGroupRequest;

public class MenuGroupFixture {

    private MenuGroupFixture() {
    }

    public static MenuGroup of(final Long id, final Name name) {
        return new MenuGroup(id, name);
    }

    public static MenuGroup of(final Name name) {
        return of(null, name);
    }

    public static MenuGroup of(final Long id, final String name) {
        return of(id, new Name(name));
    }

    public static MenuGroup of(final String name) {
        return of(null, name);
    }

    public static MenuGroupRequest ofRequest(final String name) {
        return new MenuGroupRequest(name);
    }
}
