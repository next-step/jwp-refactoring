package kitchenpos.menus.menu.dto;


import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import kitchenpos.menus.menu.domain.Menu;
import kitchenpos.menus.menu.domain.MenuProduct;

public class MenuRequest {

    private String name;
    private Long price;
    private Long menuGroupId;
    private List<MenuProductRequest> menuProductRequests;

    protected MenuRequest() {
    }

    protected MenuRequest(String name, Long price, Long menuGroupId, List<MenuProductRequest> menuProductRequests) {
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProductRequests = menuProductRequests;
    }

    protected MenuRequest(String name, Long price, Long menuGroupId) {
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
    }

    public static MenuRequest of(String name, Long price, Long menuGroupId, List<MenuProductRequest> menuProducts) {
        return new MenuRequest(name, price, menuGroupId, menuProducts);
    }

    public static MenuRequest from(Menu menu) {
        return new MenuRequest(menu.getName(), menu.getPrice().longValue(), menu.getMenuGroupId());
    }

    public Menu toMenu() {
        Menu menu = new Menu(name, BigDecimal.valueOf(price), menuGroupId);
        List<MenuProduct> menuProducts = menuProductRequests.stream().map(menuProduct ->
                new MenuProduct(
                        menu,
                        menuProduct.getProductId(),
                        menuProduct.getQuantity())
        ).collect(Collectors.toList());
        menu.addMenuProduct(menuProducts);
        return menu;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MenuRequest that = (MenuRequest) o;
        return Objects.equals(name, that.name) && Objects.equals(price, that.price)
                && Objects.equals(menuGroupId, that.menuGroupId) && Objects.equals(menuProductRequests,
                that.menuProductRequests);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, price, menuGroupId, menuProductRequests);
    }
}
