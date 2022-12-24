package kitchenpos.menu.dto;

import kitchenpos.common.vo.Name;
import kitchenpos.menu.domain.MenuGroup;

public class MenuGroupCreateRequest {

    private final String name;

    public MenuGroupCreateRequest(String name) {
        this.name = name;
    }

    public MenuGroup toMenuGroup() {
        return new MenuGroup(new Name(this.name));
    }
}
