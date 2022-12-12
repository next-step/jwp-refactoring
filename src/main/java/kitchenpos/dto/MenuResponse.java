package kitchenpos.dto;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

public class MenuResponse {
    private final Long id;
    private final String name;
    private final BigDecimal price;
    private final Long menuGroupId;
    private final List<MenuProductResponse> menuProducts;

    public MenuResponse(final Long id, final String name, final BigDecimal price,
                        final Long menuGroupId, final List<MenuProductResponse> menuProducts) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
    }

    public static MenuResponse from(final Menu menu) {
        return new MenuResponse(menu.getId(), menu.getName(), menu.getPrice(),
                menu.getMenuGroup().getId(), mapToMenuResponse(menu.getMenuProducts()));
    }

    private static List<MenuProductResponse> mapToMenuResponse(final List<MenuProduct> menuProducts) {
        return menuProducts.stream()
                .map(MenuProductResponse::from)
                .collect(Collectors.toList());
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

    public List<MenuProductResponse> getMenuProducts() {
        return menuProducts;
    }
}
