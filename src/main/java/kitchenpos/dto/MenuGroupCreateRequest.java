package kitchenpos.dto;

import kitchenpos.domain.model.MenuGroup;

/**
 * @author : leesangbae
 * @project : kitchenpos
 * @since : 2021-01-15
 */
public class MenuGroupCreateRequest {

    private String name;

    public String getName() {
        return name;
    }

    public MenuGroupCreateRequest() {
    }

    public MenuGroupCreateRequest(String name) {
        this.name = name;
    }

    public MenuGroup toEntity() {
        return new MenuGroup(name);
    }
}
