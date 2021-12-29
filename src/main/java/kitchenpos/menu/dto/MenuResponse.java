package kitchenpos.menu.dto;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class MenuResponse {

    private Long id;
    private String name;
    private BigDecimal price;
    private MenuGroupResponse menuGroup;
    private List<MenuProductResponse> menuProducts;

    public MenuResponse() {
    }

    public MenuResponse(Long id, String name, BigDecimal price, MenuGroupResponse menuGroup, List<MenuProductResponse> menuProducts) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroup = menuGroup;
        this.menuProducts = menuProducts;
    }

    public static MenuResponse of(Menu menu) {
        List<MenuProductResponse> menuProductResponses = new ArrayList<>();
        for (MenuProduct menuProduct : menu.getMenuProducts()) {
            menuProductResponses.add(MenuProductResponse.of(menuProduct));
        }

        return new MenuResponse(menu.getId(),
                menu.getName(),
                menu.getPriceValue(),
                MenuGroupResponse.of(menu.getMenuGroup()),
                menuProductResponses);
    }

    public static List<MenuResponse> ofList(List<Menu> menus) {
        List<MenuResponse> menuResponses = new ArrayList<>();
        for (Menu menu : menus) {
            menuResponses.add(MenuResponse.of(menu));
        }
        return menuResponses;
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
