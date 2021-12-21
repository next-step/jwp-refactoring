package kitchenpos.menu.dto;

import java.util.List;

public class MenuRequest {

    private Long id;
    private String name;
    private Long price;
    private Long menuGroupId;
    private List<MenuProductRequest> menuProducts;

    public MenuRequest(Long id, String name, Long price, Long menuGroupId,
        List<MenuProductRequest> menuProducts) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Long getPrice() {
        return price;
    }

    public Long getMenuGroupId() {
        return menuGroupId;
    }

    public List<MenuProductRequest> getMenuProducts() {
        return menuProducts;
    }
}
