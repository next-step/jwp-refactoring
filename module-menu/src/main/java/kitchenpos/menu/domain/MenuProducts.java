package kitchenpos.menu.domain;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Embeddable
public class MenuProducts {

    @OneToMany(mappedBy = "menu", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MenuProduct> menuProducts = new ArrayList<>();

    protected MenuProducts() {
    }

    public void addMenuProduct(Menu menu, MenuProduct menuProduct) {
        if(!menuProducts.contains(menuProduct)) {
            menuProducts.add(menuProduct);
            menuProduct.updateMenu(menu);
        }
    }

    public List<MenuProduct> getMenuProducts() {
        return menuProducts;
    }
}
