package kitchenpos.menu.domain;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Embeddable
public class MenuProducts {

    @OneToMany(mappedBy = "menu", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MenuProduct> menuProducts = new ArrayList<>();

    @Transient
    private Price sum = Price.from(BigDecimal.ZERO);

    protected MenuProducts() {}

    public void add(final MenuProduct menuProduct) {
        if (!menuProducts.contains(menuProduct)) {
            this.menuProducts.add(menuProduct);
        }
        sum = sum.add(menuProduct.getProduct().getPrice(), menuProduct.getQuantity());
    }

    public Price getSum() {
        return sum;
    }

    public List<MenuProduct> getMenuProducts() {
        return menuProducts;
    }
}
