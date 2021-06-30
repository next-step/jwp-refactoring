package kitchenpos.domain;

import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Embeddable
public class MenuProducts {
    @OneToMany(mappedBy = "menu")
    private List<MenuProduct> menuProducts = new ArrayList<>();

    protected MenuProducts() {
    }

    public MenuProducts(List<MenuProduct> menuProducts) {
        this.menuProducts = menuProducts;
    }

    public void add(MenuProduct menuProduct) {
        this.menuProducts.add(menuProduct);
    }

    public List<MenuProduct> toCollection() {
        return Collections.unmodifiableList(menuProducts);
    }

    public Price sumAmount() {
        Price amount = menuProducts.stream()
                .map(item -> item.getAmount())
                .reduce(new Price(0), (b, a) -> new Price(b.getPrice().add(a.getPrice())));

        return amount;
    }
}
