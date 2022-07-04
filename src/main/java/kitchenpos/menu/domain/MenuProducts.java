package kitchenpos.menu.domain;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Embeddable
public class MenuProducts {

    @OneToMany(mappedBy = "menu", cascade = { CascadeType.PERSIST, CascadeType.REMOVE })
    private List<MenuProduct> menuProducts = new ArrayList<>();

    public MenuProducts() {
    }

    public void addAll(Menu menu, List<MenuProduct> menuProducts) {
        this.menuProducts.addAll(menuProducts);
        menuProducts.forEach(menuProduct -> menuProduct.setMenu(menu));
    }

    public List<MenuProduct> getMenuProducts() {
        return menuProducts;
    }
}
