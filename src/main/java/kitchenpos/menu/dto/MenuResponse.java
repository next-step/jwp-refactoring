package kitchenpos.menu.dto;

import java.util.List;
import kitchenpos.menu.domain.MenuProduct;

public class MenuResponse {
    private Long id;
    private String name;
    private Long price;
    private Long menuGroupId;
    private List<MenuProduct> menuProducts;

    public MenuResponse(Long id, String name, Long price, Long menuGroupId,
                        List<MenuProduct> menuProducts) {
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

    public List<MenuProduct> getMenuProducts() {
        return menuProducts;
    }
}
