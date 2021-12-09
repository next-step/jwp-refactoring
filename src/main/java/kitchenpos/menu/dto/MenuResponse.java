package kitchenpos.menu.dto;

import kitchenpos.menu.domain.Price;
import kitchenpos.menu.domain.menu.Menu;
import kitchenpos.menu.domain.menugroup.MenuGroup;
import kitchenpos.menu.domain.menuproduct.MenuProducts;

public class MenuResponse {

    private Long id;
    private String name;
    private Price menuPrice;
    private MenuGroup menuGroup;
    private MenuProducts menuProducts;

    private MenuResponse(Menu menu) {
        this.id = menu.getId();
        this.name = menu.getName();
        this.menuPrice = menu.getMenuPrice();
        this.menuGroup = menu.getMenuGroup();
        this.menuProducts = menu.getMenuProducts();
    }

    public static MenuResponse of(Menu menu) {
        return new MenuResponse(menu);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Price getMenuPrice() {
        return menuPrice;
    }

    public MenuGroup getMenuGroup() {
        return menuGroup;
    }

    public MenuProducts getMenuProducts() {
        return menuProducts;
    }
}
