package kitchenpos.menu.dto;

import com.fasterxml.jackson.annotation.JsonGetter;

import kitchenpos.product.domain.Name;

public class MenuGroupRequest {
    private Name name;

    public MenuGroupRequest() {
    }

    public MenuGroupRequest(Name name) {
        this.name = name;
    }

    public Name getName() {
        return name;
    }

    @JsonGetter("name")
    public String name() {
        return name.value();
    }
}
