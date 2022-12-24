package kitchenpos.menu.dto;

import kitchenpos.menu.domain.Menu;
import kitchenpos.product.dto.MenuProductRequest;

import java.util.List;

public class MenuRequest {
    private String name;
    private int price;
    private Long menuGroupId;
    private List<MenuProductRequest> menuProductRequests;

    public MenuRequest() {

    }

    public MenuRequest(String name, int price, Long menuGroupId, List<MenuProductRequest> menuProductRequests) {
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProductRequests = menuProductRequests;
    }

    public static MenuRequest of(Menu menu, List<MenuProductRequest> menuProductRequests) {
        return new MenuRequest(menu.getName(), menu.getPrice().value().intValue(), menu.getMenuGroup().getId(), menuProductRequests);
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public Long getMenuGroupId() {
        return menuGroupId;
    }

    public List<MenuProductRequest> getMenuProductRequests() {
        return menuProductRequests;
    }
}