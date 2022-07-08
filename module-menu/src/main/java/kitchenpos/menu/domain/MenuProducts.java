package kitchenpos.menu.domain;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Embeddable
public class MenuProducts {

    @OneToMany(mappedBy = "menu", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MenuProduct> menuProducts = new ArrayList<>();

    protected MenuProducts() {
    }

    private MenuProducts(List<MenuProduct> menuProducts) {
        this.menuProducts = menuProducts;
    }

    public static MenuProducts of(List<MenuProduct> menuProducts) {
        return new MenuProducts(menuProducts);
    }

    public List<MenuProduct> getMenuProducts() {
        return menuProducts;
    }

    public void add(MenuProduct menuProduct) {
        menuProducts.add(menuProduct);
    }
}
