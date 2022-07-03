package kitchenpos.menu.dto;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;

import java.util.List;
import java.util.stream.Collectors;

public class MenuRequest {
    private String name;
    private int price;
    private Long menuGroupId;
    private List<MenuProductRequest> menuProducts;

    protected MenuRequest() {
    }

    private MenuRequest(String name, int price, Long menuGroupId, List<MenuProductRequest> menuProducts) {
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
    }

    public static MenuRequest of(String name, int price, Long menuGroupId, List<MenuProductRequest> menuProducts){
        return new MenuRequest(name, price, menuGroupId, menuProducts);
    }

    public Menu toMenu() {
        return Menu.of(name, price, menuGroupId, retrieveMenuProducts());
    }

    private List<MenuProduct> retrieveMenuProducts() {
        return menuProducts.stream()
                .map(menuProductRequest ->
                        MenuProduct.of(menuProductRequest.getProductId(), menuProductRequest.getQuantity())
                )
                .collect(Collectors.toList());
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
