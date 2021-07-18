package kitchenpos.menu.dto;

import java.math.BigDecimal;
import java.util.List;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;

public class MenuResponse {

    private Long id;

    private String name;

    private BigDecimal price;

    private Long menuGroupId;

    private List<MenuProduct> menuProducts;

    public MenuResponse() {
    }

    public MenuResponse(final Long id, final String name, final BigDecimal price, final Long menuGroupId,
        final List<MenuProduct> menuProducts) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
    }

    public static MenuResponse of(final Menu menu) {
        return new MenuResponse(menu.getId(), menu.getName(), menu.getPrice(),
            menu.getMenuGroupId(), null);
    }

    public static MenuResponse of(final Menu menu, final List<MenuProduct> menuProducts) {
        return new MenuResponse(menu.getId(), menu.getName(), menu.getPrice(),
            menu.getMenuGroupId(), menuProducts);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public Long getMenuGroupId() {
        return menuGroupId;
    }

    public List<MenuProduct> getMenuProducts() {
        return menuProducts;
    }
}
