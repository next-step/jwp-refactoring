package kitchenpos.menu.dto;

import com.fasterxml.jackson.annotation.JsonGetter;

import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.product.domain.Name;

public class MenuGroupResponse {
    private Long id;
    private Name name;

    public MenuGroupResponse() {
    }

    public MenuGroupResponse(Long id, Name name) {
        this.id = id;
        this.name = name;
    }

    public static MenuGroupResponse of(MenuGroup menuGroup) {
        return new MenuGroupResponse(menuGroup.getId(), menuGroup.getName());
    }

    public Long getId() {
        return id;
    }

    public Name getName() {
        return name;
    }

    @JsonGetter("name")
    public String name() {
        return name.value();
    }
}
