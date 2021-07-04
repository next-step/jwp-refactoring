package kitchenpos.menu.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import kitchenpos.product.domain.Price;

@Embeddable
public class MenuProducts {

    @OneToMany(mappedBy = "menu", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<MenuProduct> data;

    @Transient
    private Price sum = Price.ZERO;

    protected MenuProducts() {
        data = new ArrayList<>();
    }

    public MenuProducts(List<MenuProduct> data) {
        this.data = data;
    }

    public void add(MenuProduct menuProduct) {
        data.add(menuProduct);
        sum = sum.add(menuProduct.getProduct().getPrice(), menuProduct.getQuantity());
    }

    public List<MenuProduct> getData() {
        return Collections.unmodifiableList(data);
    }

    public Price getSum() {
        return sum;
    }
}
