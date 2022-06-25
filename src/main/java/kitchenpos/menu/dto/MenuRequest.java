package kitchenpos.menu.dto;

import java.util.List;
import kitchenpos.menu.domain.MenuV2;

public class MenuRequest {
    private String name;
    private Long price;
    private Long menuGroupId;
    private List<MenuProductRequest> menuProducts;

    public MenuRequest() {
    }

    public MenuRequest(String name, Long price, Long menuGroupId,
                       List<MenuProductRequest> menuProducts) {
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
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

    public MenuV2 toMenu() {
        return new MenuV2(null, this.name, this.price, this.menuGroupId, null);
    }
}
