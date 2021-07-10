package kitchenpos.dto.request;

import kitchenpos.domain.Name;
import kitchenpos.domain.menu.MenuGroupCreate;

public class MenuGroupCreateRequest {
    private String name;

    public MenuGroupCreateRequest() {
    }

    public MenuGroupCreateRequest(String name) {
        this.name = name;
    }

    public MenuGroupCreate toCreate() {
        return new MenuGroupCreate(new Name(name));
    }

    public String getName() {
        return name;
    }
}
