package kitchenpos.dto;

import kitchenpos.domain.Price;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuGroup;
import kitchenpos.domain.menu.MenuProduct;
import kitchenpos.domain.menu.Quantity;
import kitchenpos.domain.product.Product;

import static java.util.stream.Collectors.*;

import java.util.List;
import java.util.Map;

public class MenuRequest {
    private String name;
    private int price;
    private long menuGroupId;
    private List<MenuProductRequest> menuProductRequests;

    public MenuRequest() {
    }

    public MenuRequest(String name, int price, long menuGroupId, List<MenuProductRequest> menuProductRequests) {
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProductRequests = menuProductRequests;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public long getMenuGroupId() {
        return menuGroupId;
    }

    public List<MenuProductRequest> getMenuProductRequest() {
        return menuProductRequests;
    }

    public List<Long> getMenuIds() {
        return menuProductRequests.stream().map(MenuProductRequest::getProductId).collect(toList());
    }

    public void putMenuProduct(Menu menu, Map<Long, Product> products) {
        menuProductRequests.forEach(p -> menu.addMenuProduct(new MenuProduct(menu, products.get(p.getProductId()), new Quantity(p.getQuantity()))));
    }

    public Menu toMenu(MenuGroup menuGroup) {
        return new Menu(name, new Price(price), menuGroup);
    }
}
