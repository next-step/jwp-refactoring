package kitchenpos.menus.menu.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.menus.menu.domain.Menu;
import kitchenpos.menus.menu.domain.MenuProduct;

public class MenuResponse {

    private final Long id;

    private final String name;

    private final BigDecimal price;

    private final List<MenuProductResponse> menuProducts;

    private final Long menuGroupId;

    public MenuResponse(
        final Long id,
        final String name,
        final BigDecimal price,
        final List<MenuProduct> menuProducts,
        final Long menuGroupId
    ) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuProducts = MenuProductResponse.ofList(menuProducts);
        this.menuGroupId = menuGroupId;
    }

    public static List<MenuResponse> ofList(final List<Menu> menus) {
        return menus.stream()
            .map(MenuResponse::of)
            .collect(Collectors.toList());
    }

    public static MenuResponse of(final Menu menu) {
        return new MenuResponse(
            menu.getId(),
            menu.getName(),
            menu.getPrice(),
            menu.getMenuProducts(),
            menu.getMenuGroupId()
        );
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

    public List<MenuProductResponse> getMenuProducts() {
        return menuProducts;
    }

    public Long getMenuGroupId() {
        return menuGroupId;
    }
}
