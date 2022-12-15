package kitchenpos.menu.dto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProducts;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.product.domain.Product;

public class MenuRequest {

    private String name;

    private BigDecimal price;

    private Long menuGroupId;

    private List<MenuProductRequest> menuProducts;

    public MenuRequest() {}

    private MenuRequest(String name, BigDecimal price, Long menuGroupId, List<MenuProductRequest> menuProducts) {
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
    }

    public static MenuRequest of(String name, BigDecimal price, Long menuGroupId, List<MenuProductRequest> menuProducts) {
        return new MenuRequest(name, price, menuGroupId, menuProducts);
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

    public List<Long> getMenuProductIds() {
        return menuProducts.stream()
            .map(MenuProductRequest::getProductId)
            .collect(Collectors.toList());
    }

    public Menu toMenu(MenuGroup menuGroup, MenuProducts menuProducts) {
        return new Menu(name, price, menuGroup, menuProducts);
    }

    public Menu toMenu(MenuGroup menuGroup, List<Product> products) {
        return new Menu(name, price, menuGroup, createMenuProducts(products));
    }

    private MenuProducts createMenuProducts(List<Product> products) {
        return MenuProducts.from(menuProducts.stream()
            .map(menuProductRequest -> menuProductRequest.toMenuProduct(products))
            .collect(Collectors.toList())
        );
    }
}
