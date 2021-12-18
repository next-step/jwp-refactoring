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

    private MenuProducts(List<MenuProduct> menuProducts) {
        this.menuProducts = menuProducts;
    }

    public static MenuProducts of(List<MenuProduct> menuProducts) {
        return new MenuProducts(menuProducts);
    }

    public boolean isOverPrice(BigDecimal requestPrice) {
        BigDecimal totalPrice = menuProducts.stream()
                .map(MenuProduct::getOriginalPrice)
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO);

        return requestPrice.compareTo(totalPrice) > 0;
    }

    public List<MenuProduct> getMenuProducts() {
        return Collections.unmodifiableList(menuProducts);
    }

    public void add(MenuProduct menuProduct) {
        menuProducts.add(menuProduct);
    }
}
