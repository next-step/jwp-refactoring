package kitchenpos.dto;

import kitchenpos.domain.Menu;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

public class MenuResponse {
    private Long id;
    private String name;
    private BigDecimal price;
    private MenuGroupResponse menuGroup;
    private List<MenuProductResponse> menuProducts;

    public MenuResponse(Long id, String name, BigDecimal price, MenuGroupResponse menuGroup, List<MenuProductResponse> menuProducts) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroup = menuGroup;
        this.menuProducts = menuProducts;
    }

    public static MenuResponse from(Menu savedMenu) {
        return new MenuResponse(savedMenu.getId(), savedMenu.getName(), savedMenu.getPrice().getPrice(), MenuGroupResponse.of(savedMenu.getMenuGroup().getId(), savedMenu.getMenuGroup().getName()), MenuProductResponse.fromList(savedMenu.getMenuProducts()));
    }

    public static List<MenuResponse> fromList(List<Menu> menus) {
        return menus.stream()
                .map(MenuResponse::from)
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

    public MenuGroupResponse getMenuGroup() {
        return menuGroup;
    }

    public List<MenuProductResponse> getMenuProducts() {
        return menuProducts;
    }

}
