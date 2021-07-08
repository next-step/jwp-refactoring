package kitchenpos.ui.dto.menu;

import kitchenpos.domain.MenuGroup;

public class MenuGroupRequest {
    private Long id;
    private String name;

    protected MenuGroupRequest() {
    }

    private MenuGroupRequest(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static MenuGroupRequest of(String name) {
        return new MenuGroupRequest(null, name);
    }

    public static MenuGroupRequest of(MenuGroup menuGroup) {
        return new MenuGroupRequest(menuGroup.getId(), menuGroup.getName());
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public MenuGroup toMenuGroup(){
        return MenuGroup.of(id, name);
    }
}
