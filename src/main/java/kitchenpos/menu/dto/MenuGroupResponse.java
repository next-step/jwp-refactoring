package kitchenpos.menu.dto;

import java.util.List;

import kitchenpos.menu.domain.MenuGroup;

public class MenuGroupResponse {
    private Long id;
    private String name;
    private List<MenuResponse> menus;

    public static MenuGroupResponse of(MenuGroup menuGroup) {
        return new MenuGroupResponse(
            menuGroup.getId(),
            menuGroup.getName(),
            menuGroup.getMenus().mapList(MenuResponse::of));
    }

    public MenuGroupResponse() {
    }

    public MenuGroupResponse(Long id, String name, List<MenuResponse> menus) {
        this.id = id;
        this.name = name;
        this.menus = menus;
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

    public List<MenuResponse> getMenus() {
        return menus;
    }

    public void setMenus(List<MenuResponse> menus) {
        this.menus = menus;
    }
}
