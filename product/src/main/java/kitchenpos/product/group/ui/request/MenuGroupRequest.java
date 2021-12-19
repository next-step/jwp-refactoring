package kitchenpos.product.group.ui.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import kichenpos.common.domain.Name;
import kitchenpos.product.group.domain.MenuGroup;

public final class MenuGroupRequest {

    private final String name;

    @JsonCreator
    public MenuGroupRequest(@JsonProperty("name") String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public MenuGroup toEntity() {
        return MenuGroup.from(Name.from(name));
    }
}
