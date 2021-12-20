package kitchenpos.menu.domain;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Embeddable
public class MenuProducts {
    @OneToMany(mappedBy = "menu", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private List<MenuProduct> menuProducts = new ArrayList<>();

    protected MenuProducts() {
    }

    public List<MenuProduct> getList() {
        return Collections.unmodifiableList(menuProducts);
    }

    public void add(MenuProduct addMenuProduct) {
        menuProducts.add(addMenuProduct);
    }

    public void validationOverPrice(BigDecimal price) {
        BigDecimal totalPrice = menuProducts.stream()
                .map(MenuProduct::calculatePriceQuantity)
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO);

        if (price.compareTo(totalPrice) > 0) {
            throw new IllegalArgumentException();
        }
    }
}
