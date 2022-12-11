package kitchenpos.menu.domain;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class MenuProducts {
    @OneToMany(mappedBy = "menu", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MenuProduct> menuProducts = new ArrayList<>();

    protected MenuProducts() {}

    public BigDecimal getTotalPrice() {
        BigDecimal totalPrice = BigDecimal.ZERO;
        menuProducts.forEach(menuProduct -> totalPrice.add(menuProduct.getPrice()));

        return totalPrice;
    }

    public List<MenuProduct> getMenuProducts() {
        return menuProducts;
    }

    public void addMenuProduct(Menu menu, MenuProduct menuProduct) {
        if(!menuProducts.contains(menuProduct)) {
            this.menuProducts.add(menuProduct);
            menuProduct.updateMenu(menu);
        }
    }
}
