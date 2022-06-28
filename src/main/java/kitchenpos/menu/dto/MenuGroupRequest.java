package kitchenpos.menu.dto;

import javax.validation.constraints.NotEmpty;
import kitchenpos.menu.domain.MenuGroup;

public class MenuGroupRequest {
    @NotEmpty
    private String name;

    public MenuGroupRequest() {
    }

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
