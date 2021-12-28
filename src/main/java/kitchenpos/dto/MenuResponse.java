package kitchenpos.dto;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;

import java.math.BigDecimal;

public class MenuResponse {

    private final Long id;
    private final BigDecimal price;
    private final MenuGroup menuGroup;
    private final List<MenuProduct> menuProducts;

    public MenuResponse(Long id, BigDecimal price, MenuGroup menuGroup, List<MenuProduct> menuProducts) {
        this.id = id;
        this.price = price;
        this.menuGroup = menuGroup;
        this.menuProducts = menuProducts;
    }

    public static MenuResponse from(Menu menu) {
        return new MenuResponse(menu.getId(), menu.getPrice().getValue(), menu.getMenuGroup(),
            menu.getMenuProducts().getMenuProducts());
    }

    public static List<MenuResponse> from(List<Menu> menus) {
        return menus.stream()
            .map(MenuResponse::from)
            .collect(Collectors.toList());
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

    public List<MenuProduct> getMenuProducts() {
        return menuProducts;
    }
}
