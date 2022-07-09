package kitchenpos.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import kitchenpos.domain.MenuGroup;

public class CreateMenuGroupRequest {
    private final String name;

    @JsonCreator
    public CreateMenuGroupRequest(@JsonProperty(value = "name") final String name) {
        this.name = name;
    }

    public MenuGroup toEntity() {
        return MenuGroup.of(name);
    }

    public String getName() {
        return name;
    }
}
