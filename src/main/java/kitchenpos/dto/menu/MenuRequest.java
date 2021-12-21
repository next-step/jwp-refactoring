package kitchenpos.dto.menu;

import java.util.List;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuGroup;
import kitchenpos.domain.product.Product;

public class MenuRequest {

    private String name;
    private Integer price;
    private Long menuGroupId;
    private MenuProductRequests menuProducts;

    public MenuRequest() {
    }

    public MenuRequest(String name, Integer price, Long menuGroupId,
        MenuProductRequests menuProducts) {
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
    }

    public String getName() {
        return name;
    }

    public Integer getPrice() {
        return price;
    }

    public Long getMenuGroupId() {
        return menuGroupId;
    }

    public MenuProductRequests getMenuProducts() {
        return menuProducts;
    }

    public List<Long> getProductIds() {
        return menuProducts.getProductIds();
    }

    public Menu toMenu(MenuGroup menuGroup, List<Product> menuProducts) {
        return new Menu(name
            , price
            , menuGroup
            , this.menuProducts.toMenuProducts(menuProducts));
    }
}
