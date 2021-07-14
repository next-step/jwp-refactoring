package kitchenpos.menu.domain;

import kitchenpos.common.valueobject.Price;
import kitchenpos.menu.domain.exception.BadMenuPriceException;
import org.springframework.data.annotation.ReadOnlyProperty;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Embeddable
public class MenuProducts {

    @OneToMany(mappedBy = "menu")
    @ReadOnlyProperty
    private final List<MenuProduct> menuProducts;

    protected MenuProducts() {
        this.menuProducts = new ArrayList<>();
    }

    private MenuProducts(List<MenuProduct> menuProducts) {
        this.menuProducts = menuProducts;
    }

    public static MenuProducts of(List<MenuProduct> menuProducts) {
        return new MenuProducts(menuProducts);
    }

    public List<MenuProduct> getUnmodifiableList() {
        return Collections.unmodifiableList(menuProducts);
    }

    public void validatePrice(Price menuPrice) {
        Price sumPrice = Price.of(menuProducts.stream()
                .map(MenuProduct::getCalculatedPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add));
        if (menuPrice.isMoreExpensiveThan(sumPrice)) {
            throw new BadMenuPriceException();
        }
    }

    public void registerAll(Menu menu) {
        menuProducts.forEach(menuProduct -> menuProduct.registerMenu(menu));
    }
}
