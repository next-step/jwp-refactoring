package kitchenpos.menu.dto;

import java.util.List;

public class MenuRequest {
    private String name;
    private long price;
    private Long menuGroupId;
    private List<MenuProductRequest> menuProducts;

    public MenuRequest() {}

    public MenuRequest(String name, long price, Long menuGroupId, List<MenuProductRequest> menuProducts) {
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
    }

    public String getName() {
        return name;
    }

    public long getPrice() {
        return price;
    }

    public Long getMenuGroupId() {
        return menuGroupId;
    }

    public List<MenuProductRequest> getMenuProducts() {
        return menuProducts;
    }
}
