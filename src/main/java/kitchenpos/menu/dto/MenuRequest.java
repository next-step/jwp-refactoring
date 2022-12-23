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
     private List<MenuProductRequest> menuProductRequests = new ArrayList<>();

    protected MenuRequest() {}

    private MenuRequest(String name, BigDecimal price, Long menuGroupId, List<MenuProductRequest> menuProductRequests) {
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProductRequests = menuProductRequests;
    }

    public static MenuRequest of(String name, BigDecimal price, Long menuGroupId, List<MenuProductRequest> menuProducts) {
        return new MenuRequest(name, price, menuGroupId, menuProducts);
    }

    private MenuProducts createMenuProducts(List<Product> products) {
        return new MenuProducts(menuProductRequests.stream()
                .map(menuProduct -> menuProduct.createMenuProduct(products))
                .collect(Collectors.toList())
        );
    }

    public Menu toMenu(MenuGroup menuGroup, MenuProducts menuProducts) {
        return Menu.of(name, price, menuGroup.getId(), menuProducts.get());
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

    public List<MenuProductRequest> getMenuProductRequests() {
        return menuProductRequests;
    }

}
