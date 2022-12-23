package kitchenpos.menugroup.domain;

import kitchenpos.menugroup.dto.MenuGroupRequest;

public class MenuGroupFixture {
    private MenuGroupFixture() {
    }

    public static MenuGroupRequest menuGroupParam(String name) {
        return new MenuGroupRequest(name);
    }

    public static MenuGroup savedMenuGroup(Long id, String name) {
        return MenuGroup.of(id, name);
    }
}
