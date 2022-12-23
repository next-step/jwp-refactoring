package kitchenpos.menu.dto;

import kitchenpos.menu.domain.Menu;

import java.util.List;
import java.util.stream.Collectors;

public class MenuRequest {
    private String name;
    private int price;
    private Long menuGroupId;
    private List<MenuProductRequest> menuProducts;

    public MenuRequest() {

    }

    public MenuRequest(String name, int price, Long menuGroupId, List<MenuProductRequest> menuProducts) {
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
    }

    public static MenuRequest of(Menu menu) {
        List<MenuProductRequest> menuProducts = menu.getMenuProducts().stream()
                .map(menuProduct -> MenuProductRequest.of(menuProduct))
                .collect(Collectors.toList());
        return new MenuRequest(menu.getName(), menu.getPrice().intValue(), menu.getMenuGroup().getId(), menuProducts);
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setMenuGroupId(Long menuGroupId) {
        this.menuGroupId = menuGroupId;
    }

    public void setMenuProducts(List<MenuProductRequest> menuProducts) {
        this.menuProducts = menuProducts;
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

    public List<MenuProductRequest> getMenuProducts() {
        return menuProducts;
    }
}
