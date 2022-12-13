package kitchenpos.dto;

import java.math.BigDecimal;
import java.util.List;

public class MenuRequest {
    private final String name;
    private final BigDecimal price;
    private final Long menuGroupId;
    private final List<MenuProductRequest> menuProductsRequest;

    public MenuRequest(final String name, final BigDecimal price, final Long menuGroupId, final List<MenuProductRequest> menuProductsRequest) {
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProductsRequest = menuProductsRequest;
    }

    public static MenuRequest of(final String name, final BigDecimal price, final Long menuGroupId, final List<MenuProductRequest> menuProducts) {
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

    public List<MenuProductRequest> getMenuProductsRequest() {
        return menuProductsRequest;
    }
}
