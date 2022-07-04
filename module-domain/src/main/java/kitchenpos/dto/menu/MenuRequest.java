package kitchenpos.dto.menu;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuProduct;
import kitchenpos.domain.menu.MenuProducts;

public class MenuRequest {
    private String name;
    private long price;
    private Long menuGroupId;
    private List<MenuProductRequest> menuProducts;

    public MenuRequest() {}

    public MenuRequest(String name, long price, Long menuGroupId, List<MenuProductRequest> menuProducts) {
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
    }

    public String getName() {
        return name;
    }

    public long getPrice() {
        return price;
    }

    public Long getMenuGroupId() {
        return menuGroupId;
    }

    public List<MenuProductRequest> getMenuProducts() {
        return menuProducts;
    }

    public Menu toEntity() {
        MenuProducts menuProducts = mapToMenuProducts();
        return Menu.createMenu(name, price, menuGroupId, menuProducts);
    }

    private MenuProducts mapToMenuProducts() {
        List<MenuProduct> mapToMenuProducts = this.menuProducts.stream()
                .map(MenuProductRequest::toEntity)
                .collect(Collectors.toList());
        return MenuProducts.createMenuProducts(mapToMenuProducts);
    }
}
