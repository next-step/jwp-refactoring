package kitchenpos.menu.dto;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.Product;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class MenuRequest {
    private String name;
    private BigDecimal price;
    private Long menuGroupId;
    private HashMap<Long, Long> quantityOfProducts;

    public MenuRequest() {}

    public MenuRequest(String name, BigDecimal price, Long menuGroupId, HashMap<Long, Long> quantityOfProducts) {
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.quantityOfProducts = quantityOfProducts;
    }

    public Menu toMenu(MenuGroup menuGroup, List<Product> products) {
        Menu menu = new Menu(name, price, menuGroup);
        products.forEach(product -> {
            menu.addMenuProduct(new MenuProduct(menu, product, quantityOfProducts.get(product.getId())));
        });
        menu.validatePrice();

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

    public HashMap<Long, Long> getQuantityOfProducts() {
        return quantityOfProducts;
    }

    public Set<Long> findAllProductIds() {
        return quantityOfProducts.keySet();
    }
}
