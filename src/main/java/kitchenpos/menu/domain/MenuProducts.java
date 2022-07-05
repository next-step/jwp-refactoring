package kitchenpos.menu.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

@Embeddable
public class MenuProducts {
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "menu_id")
    private List<MenuProduct> values;

    public MenuProducts() {
        this(new ArrayList<>());
    }

    public MenuProducts(List<MenuProduct> menuProducts) {
        this.values = copy(menuProducts);
    }

    private static List<MenuProduct> copy(List<MenuProduct> menuProducts) {
        return menuProducts.stream()
                .map(MenuProduct::from)
                .collect(Collectors.toList());
    }

    public List<MenuProduct> value() {
        return Collections.unmodifiableList(values);
    }

    public void add(final MenuProduct menuProduct) {
        values.add(menuProduct);

    }

    public Price totalAmount() {
        Price sum = new Price();
        for(final MenuProduct menuProduct: values) {
            sum = sum.add(menuProduct.amount());
        }
        return sum;
    }
}
