package kitchenpos.domain;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Embeddable
public class MenuProducts {
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "menuId")
    private final List<MenuProduct> products = new ArrayList<>();

    protected MenuProducts() {
    }

    public List<MenuProduct> getProducts() {
        return products;
    }

    public void addAll(List<MenuProduct> menuProducts) {
        this.products.addAll(menuProducts);
    }

    public Money sumPrice() {
        return products.stream()
                .map(MenuProduct::getAmount)
                .reduce(Money.ZERO_MONEY, Money::sum);
    }

    public void add(MenuProduct menuProduct) {
        this.products.add(menuProduct);
    }
}