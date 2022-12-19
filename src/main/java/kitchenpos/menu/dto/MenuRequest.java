package kitchenpos.menu.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.common.domain.Name;
import kitchenpos.common.domain.Price;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProducts;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.product.domain.Product;

public class MenuRequest {
    private String name;
    private BigDecimal price;
    private Long menuGroupId;
    private List<MenuProductRequest> menuProducts;

    protected MenuRequest() {}

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

    public Menu createMenu(MenuGroup menuGroup, List<Product> products) {
        Menu menu = new Menu(new Name(name), new Price(price), menuGroup);
        menu.setMenuProducts(createMenuProducts(products));
        return menu;
    }

    private MenuProducts createMenuProducts(List<Product> products) {
        return new MenuProducts(menuProducts.stream()
                .map(menuProduct -> menuProduct.createMenuProduct(products))
                .collect(Collectors.toList())
        );
    }
}
