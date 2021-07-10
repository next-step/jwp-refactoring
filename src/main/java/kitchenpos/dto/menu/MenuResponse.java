package kitchenpos.dto.menu;

import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuProducts;
import kitchenpos.domain.menu.Price;
import kitchenpos.domain.menugroup.MenuGroup;

public class MenuResponse {

    private final Long id;
    private final Price price;
    private final MenuGroup menuGroup;
    private final MenuProducts menuProducts;

    public MenuResponse(Long id, Price price, MenuGroup menuGroup, MenuProducts menuProducts) {
        this.id = id;
        this.price = price;
        this.menuGroup = menuGroup;
        this.menuProducts = menuProducts;
    }

    public static MenuResponse of(Menu menu) {
        return new MenuResponse(menu.getId(), menu.getPrice(), menu.getMenuGroup(), menu.getMenuProducts());
    }

    public Long getId() {
        return id;
    }

    public Price getPrice() {
        return price;
    }

    public MenuGroup getMenuGroup() {
        return menuGroup;
    }

    public MenuProducts getMenuProducts() {
        return menuProducts;
    }
}
