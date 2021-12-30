package kitchenpos.dto.menu;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;

import java.util.List;

public class MenuRequest {
    private String name;
    private long price;
    private Long menuGroupId;
    private List<MenuProductRequest> menuProducts;

    protected MenuRequest() {
    }

    public MenuRequest(final String name, final long price, final Long menuGroupId, final List<MenuProductRequest> menuProducts) {
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
    }

    public static MenuRequest of(final String name, final long price, final Long menuGroupId, final List<MenuProductRequest> menuProducts) {
        return new MenuRequest(name, price, menuGroupId, menuProducts);
    }

    public Menu toMenu(final MenuGroup menuGroup) {
        return Menu.of(this.name, this.price, menuGroup);
    }

    public String getName() {
        return this.name;
    }

    public long getPrice() {
        return this.price;
    }

    public Long getMenuGroupId() {
        return this.menuGroupId;
    }

    public List<MenuProductRequest> getMenuProducts() {
        return menuProducts;
    }
}
