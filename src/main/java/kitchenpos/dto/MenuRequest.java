package kitchenpos.dto;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;

import java.math.BigDecimal;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class MenuRequest {
    private String name;
    private BigDecimal price;
    private Long menuGroupId;
    private List<MenuProductRequest> menuProducts;

    public MenuRequest() {}

    public MenuRequest(String name, BigDecimal price, Long menuGroupId, List<MenuProductRequest> menuProducts) {
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
    }

    public Menu toMenu(MenuGroup menuGroup, List<Product> products) {
        Menu menu = new Menu(name, price, menuGroup);
        List<MenuProduct> allMenuProducts = menuProducts.stream()
                .map(request -> request.toMenuProducts(menu, products))
                .collect(toList());
        menu.create(allMenuProducts);

        return menu;
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

    public List<Long> findAllProductIds() {
        return menuProducts.stream()
                .map(MenuProductRequest::getProductId)
                .collect(toList());
    }
}
