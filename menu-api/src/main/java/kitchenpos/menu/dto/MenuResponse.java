package kitchenpos.menu.dto;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProducts;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

public class MenuResponse {
    private final Long id;
    private final String name;
    private final BigDecimal price;
    private final Long menuGroupId;
    private final List<MenuProductResponse> menuProductResponses;

    public MenuResponse(final Long id, final String name, final BigDecimal price,
                        final Long menuGroupId, final List<MenuProductResponse> menuProductResponses) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProductResponses = menuProductResponses;
    }

    public static MenuResponse from(final Menu menu) {
        return new MenuResponse(menu.getId(), menu.getName(), menu.getPrice().value(),
                menu.getMenuGroupId(), toMenuResponse(menu.메뉴세트목록()));
    }

    private static List<MenuProductResponse> toMenuResponse(final MenuProducts menuProducts) {
        return menuProducts.value().stream()
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

    public List<MenuProductResponse> getMenuProductResponses() {
        return menuProductResponses;
    }
}
