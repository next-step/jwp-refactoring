package kitchenpos.menu.domain;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import kitchenpos.product.domain.Price;

@Embeddable
public class MenuProducts {
    @OneToMany(mappedBy = "menu", cascade = CascadeType.ALL)
    private List<MenuProduct> menuProducts = new ArrayList<>();

    protected MenuProducts() {}

    public MenuProducts(List<MenuProduct> menuProducts) {
        this.menuProducts = menuProducts;
    }

    public void addAll(Menu menu, List<MenuProduct> menuProducts) {
        for (MenuProduct menuProduct : menuProducts) {
            addMenuProduct(menu, menuProduct);
        }
    }

    private void addMenuProduct(Menu menu, MenuProduct menuProduct) {
        if (!this.menuProducts.contains(menuProduct)) {
            this.menuProducts.add(menuProduct);
        }
        menuProduct.addMenu(menu);
    }

    public List<MenuProduct> getValue() {
        return menuProducts;
    }

    public Price calculateTotalPrice() {
        Price sum = Price.ZERO;
        menuProducts.forEach(menuProduct -> sum.add(menuProduct.getProductPrice()));
        return sum;
    }
}
