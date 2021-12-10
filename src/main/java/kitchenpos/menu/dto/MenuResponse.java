package kitchenpos.menu.dto;

import kitchenpos.menu.domain.Price;
import kitchenpos.menu.domain.menu.Menu;
import kitchenpos.menu.domain.menugroup.MenuGroup;
import kitchenpos.menu.domain.menuproduct.MenuProduct;

import java.util.List;

public class MenuResponse {

    private Long id;
    private String name;
    private Price menuPrice;
    private MenuGroup menuGroup;
    private List<MenuProduct> menuProducts;

    public MenuResponse() {
    }

    private MenuResponse(Menu menu) {
        this.id = menu.getId();
        this.name = menu.getName();
        this.menuPrice = menu.getMenuPrice();
        this.menuGroup = menu.getMenuGroup();
        this.menuProducts = menu.getMenuProducts().getMenuProducts();
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

    public List<MenuProduct> getMenuProducts() {
        return menuProducts;
    }
}
