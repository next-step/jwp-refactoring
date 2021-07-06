package kitchenpos.dto.menu;

import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuGroup;
import kitchenpos.domain.menu.MenuProduct;
import kitchenpos.domain.menu.Price;

import java.math.BigDecimal;
import java.util.List;

public class MenuResponse {

    private final Long id;
    private final Price price;
    private final MenuGroup menuGroup;
    private final List<MenuProduct> menuProducts;

    public MenuResponse(Long id, Price price, MenuGroup menuGroup, List<MenuProduct> menuProducts) {
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

    public List<MenuProduct> getMenuProducts() {
        return menuProducts;
    }
}
