package kitchenpos.menu.ui.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import kitchenpos.common.domain.Name;

public final class MenuGroupRequest {

    private final String name;

    @JsonCreator
    public MenuGroupRequest(@JsonProperty("name") String name) {
        this.name = name;
    }

    public Name name() {
        return Name.from(name);
    }
}
