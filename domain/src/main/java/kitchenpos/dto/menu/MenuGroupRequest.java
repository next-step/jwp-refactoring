package kitchenpos.dto.menu;

import kitchenpos.domain.menugroup.MenuGroup;
import kitchenpos.domain.menugroup.Name;

public class MenuGroupRequest {

    private final String name;

    public MenuGroupRequest(String name) {
        this.name = name;
    }

    public MenuGroup toEntity() {
        return MenuGroup.of(Name.of(name));
    }

    public String getName() {
        return name;
    }
}
