package kitchenpos.menu.dto;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menugroup.dto.MenuGroupResponse;

import java.util.List;

import static java.util.stream.Collectors.toList;

public class MenuResponse {
    private final Long id;
    private final String name;
    private final long price;
    private final MenuGroupResponse menuGroup;
    private final List<MenuProductResponse> menuProducts;

    public MenuResponse(final Long id, final String name, final long price, final MenuGroupResponse menuGroup, final List<MenuProduct> menuProducts) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroup = menuGroup;
        this.menuProducts = menuProducts.stream()
            .map(MenuProductResponse::new)
            .collect(toList());
    }

    public MenuResponse(final Menu menu) {
        this(menu.getId(), menu.getName(), menu.getPrice().longValue(), new MenuGroupResponse(menu.getMenuGroup()), menu.getMenuProducts());
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

    public MenuGroupResponse getMenuGroup() {
        return menuGroup;
    }

    public List<MenuProductResponse> getMenuProducts() {
        return menuProducts;
    }
}
