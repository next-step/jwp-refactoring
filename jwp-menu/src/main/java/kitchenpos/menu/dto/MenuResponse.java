package kitchenpos.menu.dto;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class MenuResponse {
    private Long id;
    private String name;
    private long price;
    private MenuGroupResponse menuGroupResponse;
    private List<MenuProductResponse> menuProductResponses;

    public MenuResponse() {
    }

    private MenuResponse(final Long id, final String name, final long price, final MenuGroupResponse menuGroupResponse, final List<MenuProductResponse> menuProductResponses) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroupResponse = menuGroupResponse;
        this.menuProductResponses = menuProductResponses;
    }

    public static MenuResponse from(final Menu menu) {
        if (Objects.isNull(menu)) {
            return null;
        }
        final MenuGroupResponse menuGroupResponse = MenuGroupResponse.from(menu.getMenuGroup());
        final List<MenuProductResponse> menuProductResponses = menu.getMenuProducts()
                .toList()
                .stream()
                .map(MenuProductResponse::from)
                .collect(Collectors.toList());
        return new MenuResponse(menu.getId(), menu.getName().toName(), menu.getPrice().toLong(), menuGroupResponse, menuProductResponses);
    }

    public static List<MenuGroupResponse> from(final List<MenuGroup> menuGroups) {
        return menuGroups.stream()
                .map(MenuGroupResponse::from)
                .collect(Collectors.toList());
    }

    public static MenuResponse of(final Long id, final String name, final long price, final MenuGroupResponse menuGroupResponse, final List<MenuProductResponse> menuProducts) {
        return new MenuResponse(id, name, price, menuGroupResponse, menuProducts);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public long getPrice() {
        return price;
    }

    public MenuGroupResponse getMenuGroupResponse() {
        return menuGroupResponse;
    }

    public List<MenuProductResponse> getMenuProductResponses() {
        return menuProductResponses;
    }
}
