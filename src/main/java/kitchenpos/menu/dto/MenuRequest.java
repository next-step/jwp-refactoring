package kitchenpos.menu.dto;

import kitchenpos.common.domain.Price;
import kitchenpos.menu.domain.Menu;

import java.util.List;

public class MenuRequest {
    public static final int ZERO = 0;
    private String name;
    private Integer price;
    private Long menuGroupId;
    private List<MenuProductRequest> menuProducts;

    public MenuRequest(String name, Integer price, long menuGroupId, List<MenuProductRequest> menuProducts) {
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
    }

    public String getName() {
        return name;
    }

    public Integer getPrice() {
        return price;
    }

    public Long getMenuGroupId() {
        return menuGroupId;
    }

    public List<MenuProductRequest> getMenuProducts() {
        return menuProducts;
    }

    public Menu toMenuEntity() {
        return new Menu(this.name, Price.of(this.price), this.menuGroupId);
    }

}
