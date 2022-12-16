package kitchenpos.menugroup.domain;

import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menugroup.dto.MenuGroupRequest;

public class MenuGroupFixture {
    private MenuGroupFixture() {
    }

    public static MenuGroupRequest menuGroupParam(String name) {
        return new MenuGroupRequest(name);
    }

    public static MenuGroup savedMenuGroup(Long id, String name) {
        return new MenuGroup(id, name);
    }
}
