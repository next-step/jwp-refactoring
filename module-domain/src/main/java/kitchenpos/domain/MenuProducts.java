package kitchenpos.domain;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Embeddable
public class MenuProducts {

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "menu_id")
    private List<MenuProduct> menuProducts = new ArrayList<>();

    protected MenuProducts() {
    }

    public MenuProducts(List<MenuProduct> menuProducts) {
        this.menuProducts = menuProducts;
    }

    public List<MenuProduct> findAll() {
        return Collections.unmodifiableList(menuProducts);
    }

    protected Price findPriceSum() {
        Price priceSum = Price.zero();
        for (MenuProduct menuProduct : menuProducts) {
            priceSum.add(menuProduct.getPricePerQuantity());
        }
        return priceSum;
    }
}
