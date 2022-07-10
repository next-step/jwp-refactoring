package kitchenpos.menu.dto;

import kitchenpos.menu.domain.Menu;

import java.math.BigDecimal;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class MenuResponse {
    private Long id;
    private String name;
    private BigDecimal price;
    private Long menuGroupId;
    private List<MenuProductResponse> menuProducts;

    public MenuResponse() {}

    public MenuResponse(final Long id, final String name, final BigDecimal price, final Long menuGroupId,
                        final List<MenuProductResponse> menuProducts) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
    }

    public static MenuResponse from(final Menu menu) {
        List<MenuProductResponse> menuProducts = menu.getMenuProducts().stream()
                .map(MenuProductResponse::from)
                .collect(toList());

        return new MenuResponse(menu.getId(), menu.getName(), menu.getPrice(),
                menu.getMenuGroup().getId(), menuProducts);
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
