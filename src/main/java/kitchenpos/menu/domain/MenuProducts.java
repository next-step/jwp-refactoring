package kitchenpos.menu.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

@Embeddable
public class MenuProducts {

    @OneToMany(mappedBy = "menuId", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<MenuProduct> menuProductList;

    public MenuProducts() {
        this.menuProductList = new ArrayList<>();
    }

    private MenuProducts(List<MenuProduct> menuProductList) {
        this.menuProductList = menuProductList;
    }

    public static MenuProducts of(List<MenuProduct> menuProductList) {
        return new MenuProducts(menuProductList);
    }

    public boolean contain(MenuProduct menuProduct) {
        return menuProductList.contains(menuProduct);
    }

    public List<MenuProduct> toCollection() {
        return Collections.unmodifiableList(menuProductList);
    }

}
