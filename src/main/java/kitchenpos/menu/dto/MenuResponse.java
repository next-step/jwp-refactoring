package kitchenpos.menu.dto;

import kitchenpos.menu.domain.Menu;

import java.math.BigDecimal;
import java.util.List;

public class MenuResponse {
    private final Long id;
    private final String name;
    private final BigDecimal price;
    private final MenuGroupResponse menuGroup;
    private final List<MenuProductResponse> menuProducts;

    public MenuResponse(Long id, String name, BigDecimal price, MenuGroupResponse menuGroupResponse, List<MenuProductResponse> menuProducts) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroup = menuGroupResponse;
        this.menuProducts = menuProducts;
    }

    public static MenuResponse of(Menu menu) {
        return new MenuResponse(menu.getId(), menu.getName(), menu.getPrice(), MenuGroupResponse.of(menu.getMenuGroup()), MenuProductResponse.ofList(menu.getMenuProducts()));
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

    public MenuGroupResponse getMenuGroup() {
        return menuGroup;
    }

    public List<MenuProductResponse> getMenuProducts() {
        return menuProducts;
    }
}
