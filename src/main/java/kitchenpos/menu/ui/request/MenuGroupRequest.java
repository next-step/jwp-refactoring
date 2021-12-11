package kitchenpos.menu.ui.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import kitchenpos.common.domain.Name;
import kitchenpos.menu.domain.MenuGroup;

public final class MenuGroupRequest {

    private final String name;

    @JsonCreator
    public MenuGroupRequest(@JsonProperty("name") String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Name name() {
        return Name.from(name);
    }

    public MenuGroup toEntity() {
        MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName(name);
        return menuGroup;
    }
}
