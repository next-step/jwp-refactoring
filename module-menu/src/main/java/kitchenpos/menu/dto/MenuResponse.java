package kitchenpos.menu.dto;

import kitchenpos.menugroup.MenuGroupResponse;

import java.math.BigDecimal;
import java.util.List;

public class MenuResponse {

    private Long id;
    private String name;
    private BigDecimal price;

    private MenuGroupResponse menuGroup;
    private List<MenuProductResponse> menuProducts;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public MenuGroupResponse getMenuGroup() {
        return menuGroup;
    }

    public void setMenuGroup(MenuGroupResponse menuGroup) {
        this.menuGroup = menuGroup;
    }

    public List<MenuProductResponse> getMenuProducts() {
        return menuProducts;
    }

    public void setMenuProducts(List<MenuProductResponse> menuProducts) {
        this.menuProducts = menuProducts;
    }
}
