package kitchenpos.menu.dto;

import kitchenpos.menu.domain.MenuGroup;

public class MenuGroupRequest {
    private Long id;
    private String name;

    public MenuGroupRequest() {
    }

    public MenuGroupRequest(Long id) {
        this.id = id;
    }

    public MenuGroupRequest(String name) {
        this.name = name;
    }

    public MenuGroupRequest(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public MenuGroup toMenuGroup() {
        return new MenuGroup(id, name);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
