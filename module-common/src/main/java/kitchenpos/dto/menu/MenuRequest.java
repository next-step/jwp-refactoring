package kitchenpos.dto.menu;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuProduct;

public class MenuRequest {

    private final String name;
    private final BigDecimal price;
    private final Long menuGroupId;
    private final List<MenuProductRequest> menuProducts;

    public MenuRequest(String name, BigDecimal price, Long menuGroupId, List<MenuProductRequest> menuProducts) {
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
    }

    public static MenuRequest of(Menu menu) {
        List<MenuProductRequest> menuProductRequests = menu.getMenuProducts().stream()
            .map(MenuProductRequest::of)
            .collect(Collectors.toList());

        return new MenuRequest(menu.getName(), menu.getPrice(), menu.getMenuGroupId(), menuProductRequests);
    }

    public List<MenuProduct> getMenuProductsToEntity() {
        return menuProducts.stream()
            .map(MenuProductRequest::toMenuProduct)
            .collect(Collectors.toList());
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
