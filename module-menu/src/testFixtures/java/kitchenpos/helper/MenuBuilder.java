package kitchenpos.helper;

import java.util.List;
import kitchenpos.common.domain.Price;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuProducts;

public class MenuBuilder {
    private Long id;
    private String name;
    private int price;
    private MenuGroup menuGroup;
    private List<MenuProduct> menuProducts;

    private MenuBuilder() {
    }

    public static MenuBuilder builder() {
        return new MenuBuilder();
    }

    public MenuBuilder id(Long id) {
        this.id = id;
        return this;
    }

    public MenuBuilder name(String name) {
        this.name = name;
        return this;
    }

    public MenuBuilder price(int price) {
        this.price = price;
        return this;
    }

    public MenuBuilder menuGroup(MenuGroup menuGroup) {
        this.menuGroup = menuGroup;
        return this;
    }

    public MenuBuilder menuProducts(List<MenuProduct> menuProducts) {
        this.menuProducts = menuProducts;
        return this;
    }

    public Menu build() {
        return new Menu(id, name, new Price(price), menuGroup, new MenuProducts(menuProducts));
    }
}
