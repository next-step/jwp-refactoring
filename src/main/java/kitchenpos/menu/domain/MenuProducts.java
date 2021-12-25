package kitchenpos.menu.domain;

import com.google.common.collect.Lists;
import java.util.List;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

@Embeddable
public class MenuProducts {

    @OneToMany(mappedBy = "menu")
    private List<MenuProduct> menuProducts;

    protected MenuProducts() {
    }

    private MenuProducts(List<MenuProduct> menuProducts) {
        this.menuProducts = menuProducts;
    }

    public static MenuProducts of(List<MenuProduct> menuProducts) {
        return new MenuProducts(menuProducts);
    }

    public static MenuProducts empty() {
        return new MenuProducts(Lists.newArrayList());
    }

    public void add(MenuProduct menuProduct) {
        this.menuProducts.add(menuProduct);
    }

    public List<MenuProduct> getValues() {
        return menuProducts;
    }
}
