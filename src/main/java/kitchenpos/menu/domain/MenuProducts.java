package kitchenpos.menu.domain;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Embeddable
public class MenuProducts {
    @OneToMany(mappedBy = "menu", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<MenuProduct> menuProducts = new ArrayList<>();

    public MenuProducts() {
    }

    public MenuProducts(List<MenuProduct> menuProducts) {
        this.menuProducts = menuProducts;
    }

    public void add(MenuProduct menuProduct) {
        this.menuProducts.add(menuProduct);
    }

    public void addAll(List<MenuProduct> menuProducts) {
        this.menuProducts.addAll(menuProducts);
    }

    public List<MenuProduct> menuProducts() {
        return menuProducts;
    }

    public BigDecimal sumOfMenuProducts() {
        BigDecimal sum = BigDecimal.ZERO;
        for (final MenuProduct menuProduct : menuProducts) {
            sum = sum.add(menuProduct.menuProductPrice());
        }

        return sum;
    }
}
