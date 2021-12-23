package kitchenpos.dto.menu;

import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menugroup.MenuGroup;
import kitchenpos.domain.menu.MenuProducts;

import java.math.BigDecimal;

public class MenuResponse {

    private final Long id;
    private final BigDecimal price;
    private final MenuGroup menuGroup;
    private final MenuProducts menuProducts;

    public MenuResponse(Long id, BigDecimal price, MenuGroup menuGroup, MenuProducts menuProducts) {
        this.id = id;
        this.price = price;
        this.menuGroup = menuGroup;
        this.menuProducts = menuProducts;
    }

    public static MenuResponse from(Menu menu) {
        return new MenuResponse(menu.getId(), menu.getPrice(), menu.getMenuGroup(), menu.getMenuProducts());
    }

    public Long getId() {
        return id;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public MenuGroup getMenuGroup() {
        return menuGroup;
    }

    public MenuProducts getMenuProducts() {
        return menuProducts;
    }
}
