package kitchenpos.menu.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import kitchenpos.menu.domain.MenuGroup;

public class MenuGroupRequest {
    private String name;

    @JsonCreator
    public MenuGroupRequest(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public MenuGroup toMenuGroup() {
        return new MenuGroup(name);
    }
}
