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
    @OneToMany(mappedBy = "menu", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MenuProduct> menuProducts = new ArrayList<>();

    protected MenuProducts() {
    }

    public MenuProducts(List<MenuProduct> menuProducts) {
        this.menuProducts = menuProducts;
    }

    public void add(MenuProduct menuProduct) {
        menuProducts.add(menuProduct);
    }

    public BigDecimal getTotalPrice() {
        return menuProducts.stream()
                            .map(MenuProduct::getTotalPrice)
                            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public List<MenuProduct> getMenuProducts() {
        return menuProducts;
    }
}
