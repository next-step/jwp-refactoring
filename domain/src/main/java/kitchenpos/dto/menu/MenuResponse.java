package kitchenpos.dto.menu;

import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuProducts;

public class MenuResponse {

    private final Long id;
    private final PriceResponse price;
    private final MenuGroupResponse menuGroup;
    private final MenuProducts menuProducts;

    public MenuResponse(Long id, PriceResponse price, MenuGroupResponse menuGroup, MenuProducts menuProducts) {
        this.id = id;
        this.price = price;
        this.menuGroup = menuGroup;
        this.menuProducts = menuProducts;
    }

    public static MenuResponse of(Menu menu) {
        return new MenuResponse(menu.getId(), PriceResponse.of(menu.getPrice().getValue()), MenuGroupResponse.of(menu.getMenuGroup()), menu.getMenuProducts());
    }

    public Long getId() {
        return id;
    }

    public PriceResponse getPrice() {
        return price;
    }

    public MenuGroupResponse getMenuGroup() {
        return menuGroup;
    }

    public MenuProducts getMenuProducts() {
        return menuProducts;
    }
}
