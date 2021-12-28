package kitchenpos.dto;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import kitchenpos.domain.Menu;

public class MenuResponses {
    private final List<MenuResponse> menuResponses;

    private MenuResponses(List<MenuResponse> menuResponses) {
        this.menuResponses = menuResponses;
    }

    public static MenuResponses from(List<Menu> menus) {
        return new MenuResponses(menus.stream()
            .map(MenuResponse::from)
            .collect(Collectors.toList()));
    }

    public List<MenuResponse> getMenuResponses() {
        return Collections.unmodifiableList(menuResponses);
    }
}
