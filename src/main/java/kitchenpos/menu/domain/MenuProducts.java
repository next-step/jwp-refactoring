package kitchenpos.menu.domain;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import kitchenpos.common.domain.Price;

@Embeddable
public class MenuProducts {

    @OneToMany(fetch = FetchType.LAZY)
    private final List<MenuProduct> elements = new ArrayList<>();

    public MenuProducts() {
    }

    public Price getTotalPrice() {
        Price totalPrice = new Price(Price.MIN_PRICE);
        for (MenuProduct menuProduct : elements) {
            Price price = menuProduct.getPrice();
            totalPrice.sum(price);
        }
        return totalPrice;
    }

    public void add(MenuProduct menuProduct) {
        elements.add(menuProduct);
    }

    public List<MenuProduct> get() {
        return elements;
    }

}
