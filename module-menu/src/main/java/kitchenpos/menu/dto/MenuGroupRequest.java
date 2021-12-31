package kitchenpos.menu.dto;

import kitchenpos.menu.domain.MenuGroup;

public class MenuGroupRequest {
    private String name;
    
    private MenuGroupRequest() {
    }

    private MenuGroupRequest(String name) {
        this.name = name;
    }
    
    public static MenuGroupRequest from(String name) {
        return new MenuGroupRequest(name);
    }
    
    public MenuGroup toMenuGroup() {
        return MenuGroup.from(name);
    }

    public String getName() {
        return name;
    }

}
