package kitchenpos.menu.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import kitchenpos.common.domain.Price;

@Embeddable
public class MenuProducts {

    @OneToMany(mappedBy = "menu", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MenuProduct> menuProducts;

    protected MenuProducts() {}

    public MenuProducts(List<MenuProduct> menuProducts) {
        this.menuProducts = new ArrayList<>(menuProducts);
    }

    public static MenuProducts from(List<MenuProduct> menuProducts) {
        return new MenuProducts(menuProducts);
    }

    public List<MenuProduct> readOnlyValue() {
        return Collections.unmodifiableList(menuProducts);
    }

    public void setMenu(Menu menu) {
        menuProducts.stream().forEach(menuProduct -> menuProduct.setMenu(menu));
    }

    public Price totalPrice() {
        Price totalPrice = Price.zero();
        for (MenuProduct menuProduct : menuProducts) {
            totalPrice = totalPrice.add(menuProduct.totalPrice());
        }
        return totalPrice;
    }
}
