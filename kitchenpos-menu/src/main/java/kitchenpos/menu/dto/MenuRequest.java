package kitchenpos.menu.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;

public class MenuRequest {
    private String name;
    private BigDecimal price;
    private Long menuGroupId;
    private List<MenuProductRequest> menuProducts;

    private MenuRequest() {
    }

    public MenuRequest(Menu menu) {
        this(menu.getName(), menu.getPrice(), menu.getMenuGroup().getId(), convertMenuProductRequests(menu));
    }

    public MenuRequest(String name, BigDecimal price, Long menuGroupId,
        List<MenuProductRequest> menuProducts) {
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
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

    public List<MenuProduct> toMenuProducts() {
        return menuProducts.stream()
            .map(it -> new MenuProduct(it.getProductId(), it.getQuantity()))
            .collect(Collectors.toList());
    }

    private static List<MenuProductRequest> convertMenuProductRequests(Menu menu) {
        return menu.getMenuProducts().stream()
            .map(MenuProductRequest::new)
            .collect(Collectors.toList());
    }
}
