package kitchenpos.menu.dto;

import java.util.List;

public class MenuRequest {
    private String name;
    private long price;
    private Long menuGroupId;
    private List<MenuProductRequest> products;

    public MenuRequest() {
    }

    public MenuRequest(final String name, final long price, final Long menuGroupId, final List<MenuProductRequest> products) {
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.products = products;
    }

    public String getName() {
        return name;
    }

    public MenuRequest setName(final String name) {
        this.name = name;
        return this;
    }

    public long getPrice() {
        return price;
    }

    public MenuRequest setPrice(final long price) {
        this.price = price;
        return this;
    }

    public Long getMenuGroupId() {
        return menuGroupId;
    }

    public MenuRequest setMenuGroupId(final Long menuGroupId) {
        this.menuGroupId = menuGroupId;
        return this;
    }

    public List<MenuProductRequest> getProducts() {
        return products;
    }

    public MenuRequest setProducts(final List<MenuProductRequest> products) {
        this.products = products;
        return this;
    }
}
