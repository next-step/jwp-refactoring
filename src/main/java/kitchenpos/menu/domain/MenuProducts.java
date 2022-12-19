package kitchenpos.menu.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import kitchenpos.common.domain.Price;

@Embeddable
public class MenuProducts {
    private static final BigDecimal ZERO = BigDecimal.valueOf(0);

    @OneToMany(mappedBy = "menu", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<MenuProduct> menuProducts = new ArrayList<>();

    protected MenuProducts() {
    }

    public List<MenuProduct> getMenuProducts() {
        return Collections.unmodifiableList(menuProducts);
    }

    public void add(MenuProduct menuProduct) {
        menuProducts.add(menuProduct);
    }

    public Price totalPrice() {
        Price total = Price.from(ZERO);

        for (MenuProduct menuProduct : menuProducts) {
            total = total.sum(menuProduct.totalPrice());
        }

        return total;
    }
}
