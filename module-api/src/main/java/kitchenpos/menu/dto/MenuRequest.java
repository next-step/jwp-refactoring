package kitchenpos.menu.dto;

import static java.util.stream.Collectors.toList;

import java.util.List;
import kitchenpos.moduledomain.menu.Menu;
import kitchenpos.moduledomain.menu.MenuProducts;
import kitchenpos.moduledomain.product.Amount;

public class MenuRequest {

    private String name;
    private Long price;
    private Long menuGroupId;
    private List<MenuProductRequest> menuProductRequests;

    public MenuRequest(String name, Long price, Long menuGroupId,
        List<MenuProductRequest> menuProductRequests) {
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProductRequests = menuProductRequests;
    }

    public MenuRequest() {
    }

    public String getName() {
        return name;
    }

    public Long getPrice() {
        return price;
    }

    public Long getMenuGroupId() {
        return menuGroupId;
    }

    public List<MenuProductRequest> getMenuProductRequests() {
        return menuProductRequests;
    }

    public Menu toMenu() {
        return Menu.of(
            name,
            Amount.of(price),
            menuGroupId
        );
    }

    public MenuProducts toMenuProducts() {
        return MenuProducts.of(menuProductRequests.stream()
            .map(MenuProductRequest::toMenuProduct)
            .collect(toList()));
    }

}
