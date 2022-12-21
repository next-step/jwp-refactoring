package kitchenpos.menu.ui.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import kitchenpos.menu.domain.MenuGroup;

public class MenuGroupRequest {

	private final String name;

    @JsonCreator
    public MenuGroupRequest(@JsonProperty("name") String name) {
        this.name = name;
    }

	public String getName() {
		return name;
	}

	public MenuGroup toEntity() {
		return MenuGroup.from(name);
	}
}
