package kitchenpos.product.ui.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import kitchenpos.common.domain.Name;
import kitchenpos.product.domain.MenuGroup;

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
