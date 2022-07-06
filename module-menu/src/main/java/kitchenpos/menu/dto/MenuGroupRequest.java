package kitchenpos.menu.dto;

import static kitchenpos.common.message.ValidationMessage.NOT_EMPTY;

import javax.validation.constraints.NotEmpty;
import kitchenpos.menu.domain.MenuGroup;

public class MenuGroupRequest {
    @NotEmpty(message = NOT_EMPTY)
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
