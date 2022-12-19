package kitchenpos.menu.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.menu.domain.Menu;

public class MenuRequest {
    private String name;
    private BigDecimal price;
    private Long menuGroupId;
    private List<MenuProductRequest> menuProducts;

    public static MenuRequest from(Menu menu) {
        List<MenuProductRequest> menuProductRequests = menu.getMenuProducts().stream()
                .map(MenuProductRequest::new)
                .collect(Collectors.toList());
        return new MenuRequest(menu.getName(), menu.getPrice(), menu.getMenuGroupId(), menuProductRequests);
    }

    public MenuRequest(String name, BigDecimal price, Long menuGroupId, List<MenuProductRequest> menuProducts) {
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
    }

    public Menu toMenu() {
        Menu menu = new Menu(name, price, menuGroupId);
        menuProducts.stream()
                .map(MenuProductRequest::toMenuProduct)
                .forEach(menu::addMenuProduct);
        return menu;
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

    public List<MenuProductRequest> getMenuProducts() {
        return menuProducts;
    }
}
