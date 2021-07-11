package kitchenpos.menu.domain;

import java.util.Collections;
import java.util.List;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import kitchenpos.common.domain.Price;
import kitchenpos.menu.exception.MenuPriceExceedException;

@Embeddable
public class MenuProducts {

    @OneToMany(mappedBy = "menuId", orphanRemoval = true)
    private List<MenuProduct> menuProducts;

    public MenuProducts() {
    }

    public MenuProducts(List<MenuProduct> menuProducts, Menu menu) {
        this.menuProducts = menuProducts;
        validationMenuProductPrices(menu.getPrice());
    }

    private void validationMenuProductPrices(Price menuPrice) {
        if (menuPrice.compareTo(sumOfMenuProductPrices()) > 0) {
            throw new MenuPriceExceedException();
        }
    }

    protected Price sumOfMenuProductPrices() {
        return menuProducts.stream()
            .map(MenuProduct::getPrice)
            .reduce(new Price(0), Price::plus);
    }

    public List<MenuProduct> getMenuProducts() {
        return Collections.unmodifiableList(menuProducts);
    }
}
