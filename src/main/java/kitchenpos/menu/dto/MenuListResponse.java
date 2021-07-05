package kitchenpos.menu.dto;

import java.util.List;

public class MenuListResponse {
    List<MenuResponse> menuResponses;

    public MenuListResponse() {}

    public MenuListResponse(List<MenuResponse> menuResponses) {
        this.menuResponses = menuResponses;
    }

    public static MenuListResponse of(List<MenuResponse> menuResponses) {
        return new MenuListResponse(menuResponses);
    }

    public List<MenuResponse> getMenuResponses() {
        return menuResponses;
    }
}
