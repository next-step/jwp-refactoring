package kitchenpos.menu.domain;

import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Embeddable
public class MenuProducts {
    @OneToMany(mappedBy = "menu")
    private List<MenuProduct> menuProducts = new ArrayList<>();

    protected MenuProducts() {
    }

    public static MenuProducts empty() {
        return new MenuProducts();
    }


    public List<MenuProduct> getList() {
        return Collections.unmodifiableList(menuProducts);
    }

    public void add(MenuProduct addMenuProduct) {
        menuProducts.add(addMenuProduct);
    }

    public int size() {
        return menuProducts.size();
    }
}
