package kitchenpos.menu.domain;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

@Embeddable
public class MenuProducts {

    @OneToMany(mappedBy = "menu", cascade = CascadeType.ALL)
    private final List<MenuProduct> menuProducts = new ArrayList<>();

    public void addMenuProduct(MenuProduct menuProduct) {
        menuProducts.add(menuProduct);
    }

    public List<MenuProduct> getMenuProducts() {
        return menuProducts;
    }

    public Amounts getAmounts() {
        Amounts amounts = new Amounts();
        for (MenuProduct menuProduct : menuProducts) {
            amounts.addAmount(menuProduct.createAmount());
        }
        return amounts;
    }
}
