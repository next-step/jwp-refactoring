package kitchenpos.dto;

import kitchenpos.domain.MenuGroup;
import kitchenpos.menu.domain.MenuGroupV2;

public class MenuGroupRequest {
    private String name;

    public MenuGroupRequest() {
    }

    public MenuGroupRequest(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public MenuGroupV2 toMenuGroup() {
        return new MenuGroupV2(null, name);
    }
}
