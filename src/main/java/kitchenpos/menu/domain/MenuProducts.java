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
    @OneToMany(mappedBy = "menu", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<MenuProduct> menuProducts = new ArrayList<>();

    protected MenuProducts() {}

    public MenuProducts(List<MenuProduct> menuProducts) {
        this.menuProducts = menuProducts;
    }

    public List<MenuProduct> get() {
        return Collections.unmodifiableList(menuProducts);
    }

    public Price totalMenuPrice() {
        Price total = new Price(Price.ZERO);
        for (MenuProduct menuProduct : menuProducts) {
            total = total.add(menuProduct.calculatePrice());
        }
        return total;
    }

    public void setMenu(Menu menu) {
        this.get().forEach(menuProduct -> menuProduct.setMenu(menu));
    }
}
