package kitchenpos.menu.dto;

import kitchenpos.menu.domain.Menu;

import java.math.BigDecimal;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class MenuResponse {
    private Long id;
    private String name;
    private BigDecimal price;
    private MenuGroupResponse menuGroup;
    private List<MenuProductResponse> menuProducts;

    public MenuResponse() {}

    public MenuResponse(Menu menu) {
        this.id = menu.getId();
        this.name = menu.getName();
        this.price = menu.getPriceToDecimal();
        this.menuGroup = MenuGroupResponse.of(menu.getMenuGroup());
        this.menuProducts = MenuProductResponse.list(menu.getMenuProducts());
    }

    public static MenuResponse of(Menu menu) {
        return new MenuResponse(menu);
    }

    public static List<MenuResponse> list(List<Menu> menus) {
        return menus.stream()
                .map(MenuResponse::new)
                .collect(toList());
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
