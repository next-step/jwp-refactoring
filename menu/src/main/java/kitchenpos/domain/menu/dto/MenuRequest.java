package kitchenpos.domain.menu.dto;

import kitchenpos.domain.menu.domain.Menu;
import kitchenpos.domain.menu.domain.MenuProduct;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

public class MenuRequest {

    private String name;

    private BigDecimal price;

    private Long menuGroupId;

    private List<MenuProductRequest> menuProducts;

    public MenuRequest() {
    }

    public MenuRequest(String name, BigDecimal price, Long menuGroupId, List<MenuProductRequest> menuProducts) {
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
    }

    public Menu toMenu() {
        return new Menu(name, price, menuGroupId, toMenuProduct());
    }

    public List<MenuProduct> toMenuProduct() {
        return menuProducts.stream()
                .map(menuProductRequest -> new MenuProduct(menuProductRequest.getProductId(), menuProductRequest.getQuantity()))
                .collect(Collectors.toList());
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }
}
