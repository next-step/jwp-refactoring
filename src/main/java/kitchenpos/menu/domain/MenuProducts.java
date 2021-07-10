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
    private Price totalPrice = Price.from(BigDecimal.ZERO);

    protected MenuProducts() {}

    public void add(final MenuProduct menuProduct) {
        if (!menuProducts.contains(menuProduct)) {
            this.menuProducts.add(menuProduct);
        }
        totalPrice = totalPrice.add(menuProduct.getProduct().getPrice(), menuProduct.getQuantity());
    }

    public BigDecimal totalPrice() {
        return totalPrice.getValue();
    }

    public List<MenuProduct> getMenuProducts() {
        return menuProducts;
    }
}
