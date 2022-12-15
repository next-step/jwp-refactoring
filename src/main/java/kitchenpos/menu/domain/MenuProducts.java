package kitchenpos.menu.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import kitchenpos.common.Price;

@Embeddable
public class MenuProducts {

    @OneToMany(mappedBy = "menu", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<MenuProduct> menuProducts = new ArrayList<>();

    private MenuProducts(List<MenuProduct> menuProducts) {
        this.menuProducts = Collections.unmodifiableList(menuProducts);
    }

    protected MenuProducts() {

    }

    public static MenuProducts of(List<MenuProduct> menuProducts) {
        return new MenuProducts(menuProducts);
    }

    public List<MenuProduct> getMenuProducts() {
        return menuProducts;
    }

    public Price totalPrice() {
        Price total = Price.ZERO;
        for(MenuProduct menuProduct: menuProducts) {
            total = total.add(menuProduct.getTotalPrice());
        }
        return total;
    }

    public void updateMenuProductsMenu(Menu menu) {
        menuProducts.forEach(menuProduct -> menuProduct.updateMenu(menu));
    }
    public int getSize(){
        return menuProducts.size();
    }
}
